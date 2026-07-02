package org.danilopianini.plagiarismdetector.caching

import java.io.File
import kotlin.io.path.relativeTo
import org.apache.commons.io.FileUtils
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git
import org.slf4j.LoggerFactory

/**
 * A file based [KnowledgeBaseManager] which caches data on files.
 */
class FileKnowledgeBaseManager internal constructor(
    private val repositoryFolder: File = defaultRepositoryFolder(),
    private val sharedKnowledgeBase: SharedKnowledgeBase? = null,
    private val shouldUseSharedKnowledgeBase: (Repository) -> Boolean = { true },
) : KnowledgeBaseManager {
    constructor() : this(
        defaultRepositoryFolder(),
        defaultSharedKnowledgeBase,
        ::isSharedCacheEligible,
    )

    private val logger = LoggerFactory.getLogger(javaClass)
    private val separator = File.separator

    override fun save(project: Repository) = with(File(repositoryFolder.path + separator + project.name)) {
        if (isAvailableLocalCache()) {
            return@with
        }
        val shared = sharedKnowledgeBase?.takeIf { shouldUseSharedKnowledgeBase(project) }
        shared?.restore(project, this)
        if (isAvailableLocalCache()) {
            return@with
        }
        logger.debug("Cloning ${project.name}")
        FileUtils.deleteQuietly(this)
        clone(project, this)
        clean(this)
        shared?.store(project, this)
    }

    private fun clone(project: Repository, out: File) {
        Git.cloneRepository()
            .setURI("${project.cloneUrl}")
            .setDirectory(out)
            .call()
            .close()
    }

    private fun clean(out: File) {
        out.walkBottomUp()
            .filter { it.isFile }
            .filterNot { file ->
                file.toPath().relativeTo(out.toPath()).any { it.toString() == SOURCE_FOLDER }
            }
            .forEach(FileUtils::deleteQuietly)
        out.walkBottomUp()
            .filter { it.isDirectory && it != out && FileUtils.isEmptyDirectory(it) }
            .forEach(FileUtils::deleteQuietly)
    }

    private fun File.isAvailableLocalCache(): Boolean = isDirectory && !FileUtils.isEmptyDirectory(this)

    override fun isCached(project: Repository): Boolean = with(File(repositoryFolder.path + separator + project.name)) {
        isAvailableLocalCache()
    }

    override fun load(project: Repository): File {
        require(isCached(project)) { "${project.name} not in cache!" }
        return File(repositoryFolder.path + separator + project.name)
    }

    private companion object {
        private const val SOURCE_FOLDER = "src"
        private const val REPOSITORY_FOLDER_NAME = ".code-plagiarism-detector"
        private const val GITHUB_HOST = "github.com"

        private val defaultSharedKnowledgeBase: SharedKnowledgeBase by lazy {
            SharedKnowledgeBase(defaultRepositoryFolder())
        }

        private fun defaultRepositoryFolder(): File = File(
            System.getProperty("user.home") + File.separator + REPOSITORY_FOLDER_NAME,
        )

        private fun isSharedCacheEligible(project: Repository): Boolean =
            project.cloneUrl.host.equals(GITHUB_HOST, ignoreCase = true)
    }
}
