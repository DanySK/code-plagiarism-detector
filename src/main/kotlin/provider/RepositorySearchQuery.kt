package provider

import java.net.URL

/**
 * An interface modeling a repository search query.
 */
interface RepositorySearchQuery {
    /**
     * Search a repository by a given link.
     * @param url the [URL] address of the public repository.
     * @return the [RepositoryQueryResult]
     */
    fun byLink(url: URL): RepositoryQueryResult

    /**
     * Search a repository according to criteria.
     * @return [RepositoryQueryCriteria]
     */
    fun byCriteria(): RepositoryQueryCriteria

    /**
     * An interface modeling the search criteria for repositories.
     */
    interface RepositoryQueryCriteria {
        /**
         * Search repositories by the given name.
         * @return this
         */
        fun byName(repositoryName: String): RepositoryQueryCriteria

        /**
         * Search repositories in the list of the given user.
         * @return this
         */
        fun byUser(user: String): RepositoryQueryCriteria

        /**
         * @return the [RepositoryQueryResult]
         */
        fun search(): RepositoryQueryResult
    }

    /**
     * An interface holding the result of the search query.
     */
    interface RepositoryQueryResult {
        /**
         * The [Iterable] of [Repository] matching the query.
         */
        val result: Iterable<Repository>
    }
}
