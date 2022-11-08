package org.danilopianini.plagiarismdetector.caching

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.NameFileFilter
import org.apache.commons.io.filefilter.NotFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.File

class FileKnowledgeBaseManager : KnowledgeBaseManager {
    private val separator = System.getProperty("file.separator")
    private val homeDirectory = System.getProperty("user.home")
    private val repositoryFolder = File(homeDirectory + separator + REPOSITORY_FOLDER_NAME)

    override fun save(projectName: String, projectDirectory: File) {
        val directory = File(repositoryFolder.path + separator + projectName)
        val sources = FileUtils.listFilesAndDirs(
            projectDirectory,
            NotFileFilter(TrueFileFilter.INSTANCE),
            NameFileFilter("src")
        )
        sources.remove(projectDirectory)
        sources.forEach { FileUtils.copyDirectory(it, directory) }
    }

    override fun isCached(projectName: String) =
        File(repositoryFolder.path + separator + projectName).exists()

    override fun load(projectName: String): File {
        require(isCached(projectName))
        return File(repositoryFolder.path + separator + projectName)
    }

    companion object {
        private const val REPOSITORY_FOLDER_NAME = ".code-plagiarism-detector"
    }
}
