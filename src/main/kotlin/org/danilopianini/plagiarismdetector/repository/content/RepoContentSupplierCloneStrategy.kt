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

    private val repoName = cloneUrl.path.substringAfterLast(URL_SEPARATOR)
    private val knowledgeBaseManager = FileKnowledgeBaseManager()

    init {
        if (!knowledgeBaseManager.isCached(repoName)) {
            val tmpContentDir = Files.createTempDirectory(cloneUrl.path.substringAfterLast(URL_SEPARATOR)).toFile()
            Git.cloneRepository()
                .setURI("$cloneUrl")
                .setDirectory(tmpContentDir)
                .call()
            knowledgeBaseManager.save(repoName, tmpContentDir)
            FileUtils.deleteDirectory(tmpContentDir)
        }
    }

    override fun filesMatching(pattern: Regex): Sequence<File> = runCatching {
        FileUtils.listFiles(
            knowledgeBaseManager.load(repoName),
            RegexFileFilter(pattern.toPattern()),
            DirectoryFileFilter.DIRECTORY
        ).asSequence()
    }.getOrDefault(emptySequence())

    companion object {
        private const val URL_SEPARATOR = "/"
    }
}
