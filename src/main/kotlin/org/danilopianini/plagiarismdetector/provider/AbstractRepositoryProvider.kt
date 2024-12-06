package org.danilopianini.plagiarismdetector.provider

import org.danilopianini.plagiarismdetector.provider.criteria.SearchCriteria
import org.danilopianini.plagiarismdetector.repository.Repository
import java.net.URL

/**
 * A common base implementation to all concrete [RepositoryProvider].
 */
abstract class AbstractRepositoryProvider<I, O, in C : SearchCriteria<I, O>> : RepositoryProvider<I, O, C> {
    private companion object {
        private const val EXPECTED_PATH_ARGS = 2
    }

    override fun byLink(url: URL): Repository {
        require(urlIsValid(url)) { "$url is not valid: should point to the repository service." }
        require(urlIsWellFormed(url)) { "$url must be in `owner/name` format." }
        return getRepoByUrl(url)
    }

    /**
     * Checks if the given url is valid, i.e. it points to the repository service.
     * @param url the [URL] to validate.
     * @return true if the url is valid, false otherwise.
     */
    protected abstract fun urlIsValid(url: URL): Boolean

    /**
     * Get the expected repo.
     * @param url the repo [URL] address.
     * @return the expected [Repository] or null if not exists.
     */
    protected abstract fun getRepoByUrl(url: URL): Repository

    private fun urlIsWellFormed(url: URL): Boolean =
        url.path
            .replace(Regex("^/|/$"), "")
            .split("/")
            .count() == EXPECTED_PATH_ARGS
}
