package org.danilopianini.plagiarismdetector.repository.content

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.RegexFileFilter
import org.danilopianini.plagiarismdetector.caching.FileKnowledgeBaseManager
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
    private val repoContentDirectory: File
    private val knowledgeBaseManager = FileKnowledgeBaseManager()

    init {
        val repoName = cloneUrl.path.substringAfterLast(URL_SEPARATOR)
        if (knowledgeBaseManager.isCached(repoName)) {
            repoContentDirectory = knowledgeBaseManager.load(repoName)
        } else {
            val tmpContentDir = Files.createTempDirectory(cloneUrl.path.substringAfterLast(URL_SEPARATOR)).toFile()
            Git.cloneRepository()
                .setURI("$cloneUrl")
                .setDirectory(tmpContentDir)
                .call()
            repoContentDirectory = knowledgeBaseManager.save(repoName, tmpContentDir)
            FileUtils.deleteDirectory(tmpContentDir)
        }
    }

    override fun filesMatching(pattern: Regex): Sequence<File> =
        FileUtils.listFiles(
            repoContentDirectory,
            RegexFileFilter(pattern.toPattern()),
            DirectoryFileFilter.DIRECTORY
        ).asSequence()
}
