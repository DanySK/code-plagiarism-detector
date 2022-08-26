package org.danilopianini.plagiarismdetector.provider.criteria

/**
 * An interface modeling a search criteria.
 * @param T the result type returned applying the criteria.
 */
interface SearchCriteria<T> {
    /**
     * Apply the criteria.
     */
    fun apply(): T
}
