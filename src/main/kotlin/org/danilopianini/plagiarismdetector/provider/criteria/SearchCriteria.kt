package org.danilopianini.plagiarismdetector.provider.criteria

/**
 * An interface modeling a search criteria.
 * @param I the subject type.
 * @param O the result type returned applying the criteria.
 */
interface SearchCriteria<in I, out O> : (I) -> O
