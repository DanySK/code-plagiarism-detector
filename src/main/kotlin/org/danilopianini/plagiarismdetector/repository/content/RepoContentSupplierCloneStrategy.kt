package org.danilopianini.plagiarismdetector.repository.content

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.SuffixFileFilter
import org.eclipse.jgit.api.Git
import java.io.File
import java.net.URL
import java.nio.file.Files

/**
 * A strategy which locally clones the repository directory to supply its content.
 * @property cloneUrl the repository [URL] to clone.
 */
class RepoContentSupplierCloneStrategy(private val cloneUrl: URL) : RepoContentSupplierStrategy {
    companion object {
        private const val URL_SEPARATOR = "/"
    }
    private val clonedRepoDirectory: File

    init {
        clonedRepoDirectory = cloneRepo()
    }

    private fun cloneRepo(): File {
        val tmpDir = Files.createTempDirectory(cloneUrl.path.substringAfterLast(URL_SEPARATOR)).toFile()
        Git.cloneRepository()
            .setURI(cloneUrl.toString())
            .setDirectory(tmpDir)
            .call()
        /* In order to work, no additional files and/or folders must be created
         * inside the directory after forceDeleteOnExit() is called. */
        FileUtils.forceDeleteOnExit(tmpDir)
        return tmpDir
    }

    override fun getFilesOf(extensions: Iterable<String>): Iterable<File> = listSources(extensions)

    private fun listSources(targetExtensions: Iterable<String>): Collection<File> {
        return FileUtils.listFiles(
            clonedRepoDirectory,
            SuffixFileFilter(targetExtensions.toList()),
            DirectoryFileFilter.DIRECTORY
        )
    }
}
