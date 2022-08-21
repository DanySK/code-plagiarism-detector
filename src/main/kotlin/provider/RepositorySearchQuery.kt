package provider

import java.net.URL

/**
 * An interface modeling a repository search query.
 * @param C the type of the search criteria.
 * @param T the result type returned applying the criteria.
 */
interface RepositorySearchQuery<T, in C : SearchCriteria<T>> {
    /**
     * Search a repository by the given [URL].
     * @param url the [URL] address in `owner/name` format of the public repository to retrieve.
     * @return the requested [Repository] if exists, or null if not.
     * @throws IllegalArgumentException if the [url] doesn't match the `owner/name` pattern or
     * doesn't point to a repository service.
     */
    fun byLink(url: URL): Repository?

    /**
     * Search repositories matching the given criteria.
     * @param criteria the criteria to apply.
     * @return an [Iterable] of [Repository] matching the criteria given in input.
     * If no results is found an empty iterable is returned.
     */
    fun byCriteria(criteria: C): Iterable<Repository>
}

/**
 * An interface modeling a search criteria.
 * @param T the result type returned applying the criteria.
 */
interface SearchCriteria<T> {
    /**
     * Apply the criteria.
     * @return T the result of applying the criteria.
     */
    fun apply(): T
}
