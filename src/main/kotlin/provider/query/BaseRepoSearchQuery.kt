package provider.query

import provider.repository.Repository
import java.net.URL

private const val EXPECTED_PATH_ARGS = 2

/**
 * A common base implementation to all concrete RepositorySearchQuery.
 */
abstract class BaseRepoSearchQuery<T, in C : SearchCriteria<T>> : RepositorySearchQuery<T, C> {
    override fun byLink(url: URL): Repository? {
        if (!urlIsValid(url)) {
            throw IllegalArgumentException("The given URL is not valid: should point to a repository service.")
        } else if (!urlIsWellFormed(url)) {
            throw java.lang.IllegalArgumentException("The given URL must be in `owner/name` format.")
        }
        return getRepoByUrl(url)
    }

    /**
     * Checks if the given url is valid, i.e., it points to a repository service.
     * @param url the [URL] to validate.
     * @return true if the url is valid, false otherwise.
     */
    protected abstract fun urlIsValid(url: URL): Boolean

    /**
     * Get the expected repo.
     * @param url the repo [URL] address.
     * @return the expected [Repository] or null if not exists.
     */
    protected abstract fun getRepoByUrl(url: URL): Repository?

    private fun urlIsWellFormed(url: URL): Boolean =
        url.path.removePrefix("/").removeSuffix("/").split("/").count() == EXPECTED_PATH_ARGS
}
