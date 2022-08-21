package provider

import java.net.URL

/**
 * An interface modeling a repository search query.
 */
interface RepositorySearchQuery<T, in C : SearchCriteria<T>> {
    /**
     * Search a repository by a given link.
     * @param url the [URL] address of the public repository to retrieve.
     */
    fun byLink(url: URL): Repository?

    /**
     * Search a repository according to criteria.
     */
    fun byCriteria(criteria: C): Iterable<Repository>
}

/**
 * An interface modeling a search criteria.
 * @param T
 */
interface SearchCriteria<T> {
    /**
     * Apply the criteria.
     */
    fun apply(): T
}
