package org.danilopianini.plagiarismdetector.caching

import java.io.File
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import kotlin.use
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.FileFileFilter
import org.apache.commons.io.filefilter.FileFilterUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.RefSpec
import org.eclipse.jgit.transport.URIish
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.slf4j.LoggerFactory

internal class SharedKnowledgeBase(
    private val localCacheRoot: File,
    private val remoteUri: String = DEFAULT_REMOTE_URI,
    private val branch: String = DEFAULT_BRANCH,
    private val getenv: (String) -> String? = System::getenv,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val workingTree = File(localCacheRoot, REMOTE_CACHE_FOLDER_NAME)
    private val projectLocks = ConcurrentHashMap<String, Any>()

    @Volatile
    private var mirrorInitialized = false

    fun restore(project: Repository, destination: File): Boolean = runCatching {
        synchronized(projectLock(project)) {
            ensureMirrorInitialized()
            val cachedProject = cachedProject(project)
            if (cachedProject.isDirectory && !FileUtils.isEmptyDirectory(cachedProject)) {
                FileUtils.copyDirectory(cachedProject, destination)
                true
            } else {
                false
            }
        }
    }.getOrElse {
        logger.warn("Unable to restore ${project.owner}/${project.name} from the shared cache: ${it.message}")
        logger.debug("Unable to restore ${project.owner}/${project.name} from the shared cache.", it)
        false
    }

    fun store(project: Repository, source: File) {
        runCatching {
            synchronized(projectLock(project)) {
                ensureMirrorInitialized()
                synchronized(SHARED_CACHE_LOCK) {
                    if (source.isDirectory && !FileUtils.isEmptyDirectory(source)) {
                        open().use { git ->
                            pull(git)
                            val cachedProject = cachedProject(project)
                            FileUtils.deleteQuietly(cachedProject)
                            copyCacheableFiles(source, cachedProject)
                            git.add().addFilepattern(REPOSITORIES_FOLDER).call()
                            git.add().setUpdate(true).addFilepattern(REPOSITORIES_FOLDER).call()
                            if (git.repository.resolve(HEAD) == null || git.status().call().hasUncommittedChanges()) {
                                git.commit()
                                    .setAuthor(COMMITTER_NAME, COMMITTER_EMAIL)
                                    .setCommitter(COMMITTER_NAME, COMMITTER_EMAIL)
                                    .setMessage("Cache ${project.owner}/${project.name}")
                                    .setSign(false)
                                    .call()
                                push(git)
                            }
                        }
                    }
                }
            }
        }.onFailure {
            logger.warn("Unable to store ${project.owner}/${project.name} in the shared cache: ${it.message}")
            logger.debug("Unable to store ${project.owner}/${project.name} in the shared cache.", it)
        }
    }

    private fun ensureMirrorInitialized() {
        if (!mirrorInitialized) {
            synchronized(SHARED_CACHE_LOCK) {
                if (!mirrorInitialized) {
                    logger.info("Initializing shared source cache from $remoteUri")
                    open().use { pull(it) }
                    mirrorInitialized = true
                    logger.info("Shared source cache initialized")
                }
            }
        }
    }

    private fun open(): Git {
        localCacheRoot.mkdirs()
        return when {
            File(workingTree, GIT_FOLDER).isDirectory -> Git.open(workingTree)
            hasRemoteRefs() -> Git.cloneRepository()
                .setURI(remoteUri)
                .setBranch(branch)
                .setDirectory(workingTree)
                .setTimeout(GIT_OPERATION_TIMEOUT_SECONDS)
                .call()
            else -> Git.init()
                .setDirectory(workingTree)
                .setInitialBranch(branch)
                .call()
                .also { it.remoteAdd().setName(ORIGIN).setUri(URIish(remoteUri)).call() }
        }
    }

    private fun pull(git: Git) {
        if (hasRemoteRefs()) {
            git.pull()
                .setRemote(ORIGIN)
                .setRemoteBranchName(branch)
                .setCredentialsProvider(credentialsProvider())
                .setTimeout(GIT_OPERATION_TIMEOUT_SECONDS)
                .call()
        }
    }

    private fun push(git: Git) {
        val credentialsProvider = credentialsProvider()
        if (credentialsProvider == null) {
            logger.warn(
                "Skipping shared cache upload because neither $GITHUB_TOKEN nor $GITHUB_ACTIONS_TOKEN is set.",
            )
            return
        }
        git.push()
            .setRemote(ORIGIN)
            .setRefSpecs(RefSpec("refs/heads/$branch:refs/heads/$branch"))
            .setCredentialsProvider(credentialsProvider)
            .setTimeout(GIT_OPERATION_TIMEOUT_SECONDS)
            .call()
    }

    private fun cachedProject(project: Repository): File = File(
        workingTree,
        listOf(REPOSITORIES_FOLDER, project.cloneUrl.cacheHost(), project.owner, project.name)
            .joinToString(File.separator) { it.sanitizePathSegment() },
    )

    private fun copyCacheableFiles(source: File, destination: File) {
        FileUtils.copyDirectory(
            source,
            destination,
            FileFilterUtils.or(
                FileFileFilter.INSTANCE,
                object : IOFileFilter by DirectoryFileFilter.DIRECTORY {
                    override fun accept(file: File): Boolean = file.name != GIT_FOLDER
                    override fun accept(dir: File, name: String): Boolean = name != GIT_FOLDER
                },
            ),
        )
    }

    private fun hasRemoteRefs(): Boolean = Git.lsRemoteRepository()
        .setRemote(remoteUri)
        .setCredentialsProvider(credentialsProvider())
        .setTimeout(GIT_OPERATION_TIMEOUT_SECONDS)
        .call()
        .isNotEmpty()

    private fun credentialsProvider(): UsernamePasswordCredentialsProvider? =
        (getenv(GITHUB_TOKEN) ?: getenv(GITHUB_ACTIONS_TOKEN))
            ?.let { UsernamePasswordCredentialsProvider(GITHUB_USERNAME, it) }

    private fun String.sanitizePathSegment(): String = replace(Regex("""[^A-Za-z0-9._-]"""), "_")

    private fun URL.cacheHost(): String = host.ifBlank { protocol }

    private fun projectLock(project: Repository): Any = projectLocks.computeIfAbsent(project.cacheKey()) { Any() }

    private fun Repository.cacheKey(): String = listOf(
        cloneUrl.cacheHost(),
        owner,
        name,
    ).joinToString("/")

    private companion object {
        private const val DEFAULT_REMOTE_URI = "https://github.com/unibo-oop/plagiarism-cache.git"
        private const val DEFAULT_BRANCH = "main"
        private const val REMOTE_CACHE_FOLDER_NAME = ".remote-cache"
        private const val REPOSITORIES_FOLDER = "repositories"
        private const val GIT_FOLDER = ".git"
        private const val ORIGIN = "origin"
        private const val HEAD = "HEAD"
        private const val GITHUB_TOKEN = "GH_TOKEN"
        private const val GITHUB_ACTIONS_TOKEN = "GITHUB_TOKEN"
        private const val GITHUB_USERNAME = "x-access-token"
        private const val GIT_OPERATION_TIMEOUT_SECONDS = 120
        private const val COMMITTER_NAME = "code-plagiarism-detector"
        private const val COMMITTER_EMAIL = "noreply@github.com"
        private val SHARED_CACHE_LOCK = Any()
    }
}
