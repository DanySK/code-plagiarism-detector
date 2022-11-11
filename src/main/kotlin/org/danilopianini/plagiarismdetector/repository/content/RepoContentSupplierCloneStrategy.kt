package org.danilopianini.plagiarismdetector.repository.content

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.RegexFileFilter
import org.danilopianini.plagiarismdetector.caching.FileKnowledgeBaseManager
import org.danilopianini.plagiarismdetector.repository.Repository
import java.io.File

/**
 * A strategy which demands to a [FileKnowledgeBaseManager]
 * the caching of [repository] sources.
 */
class RepoContentSupplierCloneStrategy(private val repository: Repository) : RepoContentSupplierStrategy {

    private val knowledgeBaseManager = FileKnowledgeBaseManager().also {
        if (!it.isCached(repository)) {
            it.save(repository)
        }
    }
    private val contentDirectory by lazy {
        knowledgeBaseManager.load(repository)
    }

    override fun filesMatching(pattern: Regex): Sequence<File> =
        FileUtils.listFiles(
            contentDirectory,
            RegexFileFilter(pattern.toPattern()),
            DirectoryFileFilter.DIRECTORY
        ).asSequence()
}
