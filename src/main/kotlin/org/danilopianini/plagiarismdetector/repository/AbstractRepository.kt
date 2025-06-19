package org.danilopianini.plagiarismdetector.repository

import java.io.File
import org.danilopianini.plagiarismdetector.repository.content.RepoContentSupplierImpl

/**
 * Abstract base implementation for repositories.
 */
abstract class AbstractRepository : Repository {
    private val repositoryContentSupplier by lazy {
        RepoContentSupplierImpl(this)
    }

    override fun getSources(pattern: Regex): Sequence<File> = repositoryContentSupplier.filesMatching(pattern)
}
