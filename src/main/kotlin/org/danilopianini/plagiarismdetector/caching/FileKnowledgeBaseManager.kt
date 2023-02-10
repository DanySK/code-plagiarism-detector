package org.danilopianini.plagiarismdetector.caching

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.NameFileFilter
import org.apache.commons.io.filefilter.NotFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git
import org.slf4j.LoggerFactory
import java.io.File

/**
 * A file based [KnowledgeBaseManager] which caches data on files.
 */
class FileKnowledgeBaseManager : KnowledgeBaseManager {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val separator = System.getProperty("file.separator")
    private val homeDirectory = System.getProperty("user.home")
    private val repositoryFolder by lazy {
        File(homeDirectory + separator + REPOSITORY_FOLDER_NAME)
    }

    override fun save(project: Repository) =
        with(File(repositoryFolder.path + separator + project.name)) {
            logger.debug("Cloning ${project.name}")
            clone(project, this)
            clean(this)
        }

    private fun clone(project: Repository, out: File) = runCatching {
        Git.cloneRepository()
            .setURI("${project.cloneUrl}")
            .setDepth(1)
            .setDirectory(out)
            .call()
            .close()
    }.getOrElse { logger.error(it.message) }

    private fun clean(out: File) {
        val matching = FileUtils.listFiles(
            out,
            TrueFileFilter.INSTANCE,
            NotFileFilter(NameFileFilter(SOURCE_FOLDER))
        )
        matching.forEach { FileUtils.deleteQuietly(it) }
    }

    override fun isCached(project: Repository): Boolean =
        with(File(repositoryFolder.path + separator + project.name)) {
            isDirectory && !FileUtils.isEmptyDirectory(this)
        }

    override fun load(project: Repository): File {
        require(isCached(project)) { "${project.name} not in cache!" }
        return File(repositoryFolder.path + separator + project.name)
    }

    companion object {
        private const val REPOSITORY_FOLDER_NAME = ".code-plagiarism-detector"
        private const val SOURCE_FOLDER = "src"
    }
}
