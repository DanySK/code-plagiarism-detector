package org.danilopianini.plagiarismdetector.repository.content

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.RegexFileFilter
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
        clonedRepoDirectory = Files.createTempDirectory(cloneUrl.path.substringAfterLast(URL_SEPARATOR)).toFile()
        Git.cloneRepository()
            .setURI("$cloneUrl")
            .setDirectory(clonedRepoDirectory)
            .call()
        /* In order to work, no additional files and/or folders must be created
         * inside the directory after forceDeleteOnExit() is called. */
        FileUtils.forceDeleteOnExit(clonedRepoDirectory)
    }

    override fun filesMatching(pattern: Regex): Sequence<File> =
        FileUtils.listFiles(
            clonedRepoDirectory,
            RegexFileFilter(pattern.toPattern()),
            DirectoryFileFilter.DIRECTORY
        ).asSequence()
}
