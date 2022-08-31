package org.danilopianini.plagiarismdetector.provider

import org.danilopianini.plagiarismdetector.provider.criteria.SearchCriteria
import org.danilopianini.plagiarismdetector.repository.Repository
import java.net.URL

/**
 * An interface modeling a provider of [Repository].
 * @param C the type of the search criteria.
 */
interface RepositoryProvider<I, O, in C : SearchCriteria<I, O>> {
    /**
     * Search a repository by the given [URL].
     * @param url the [URL] address in `owner/name` format of the public repository to retrieve.
     * @return the requested [Repository] if exists.
     * @throws IllegalArgumentException if the [url] doesn't match the `owner/name` pattern or is not correct.
     */
    fun byLink(url: URL): Repository

    /**
     * Search repositories matching the given criteria.
     * @param criteria the criteria to apply.
     * @return an [Iterable] of [Repository] matching the criteria given in input.
     * @throws IllegalArgumentException if the user do not exist.
     */
    fun byCriteria(criteria: C): Iterable<Repository>
}
