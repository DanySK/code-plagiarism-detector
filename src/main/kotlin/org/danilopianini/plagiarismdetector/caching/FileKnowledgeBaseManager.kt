package org.danilopianini.plagiarismdetector.caching

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.NameFileFilter
import org.apache.commons.io.filefilter.NotFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git
import java.io.File

/**
 * A file based knowledge base manager which caches data on files.
 */
class FileKnowledgeBaseManager : KnowledgeBaseManager {
    private val separator = System.getProperty("file.separator")
    private val homeDirectory = System.getProperty("user.home")
    private val repositoryFolder = File(homeDirectory + separator + REPOSITORY_FOLDER_NAME)

    override fun save(project: Repository) =
        with(File(repositoryFolder.path + separator + project.name)) {
            clone(project, this)
            clean(this)
        }

    private fun clone(project: Repository, out: File) {
        Git.cloneRepository()
            .setURI("${project.cloneUrl}")
            .setDirectory(out)
            .call()
    }

    private fun clean(out: File) {
        val matching = FileUtils.listFiles(
            out,
            TrueFileFilter.INSTANCE,
            NotFileFilter(NameFileFilter(SOURCE_FOLDER))
        )
        matching.forEach { FileUtils.delete(it) }
    }

    override fun isCached(project: Repository): Boolean =
        with(File(repositoryFolder.path + separator + project.name)) {
            isDirectory && !FileUtils.isEmptyDirectory(this)
        }

    override fun load(project: Repository): File {
        require(isCached(project)) { "${project.name} not in cache!" }
        return File(repositoryFolder.path + separator + project.name)
    }

    override fun clean(project: Repository) = FileUtils.deleteDirectory(
        File(repositoryFolder.path + separator + project.name)
    )

    override fun cleanAll() = FileUtils.cleanDirectory(repositoryFolder)

    companion object {
        private const val REPOSITORY_FOLDER_NAME = ".code-plagiarism-detector"
        private const val SOURCE_FOLDER = "src"
    }
}
