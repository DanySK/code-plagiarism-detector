package org.danilopianini.plagiarismdetector.detector

/**
 * An interface modeling a comparison result object.
 */
interface ComparisonResult<out M : Match> {
    /**
     * The score of similarity.
     */
    val scoreOfSimilarity: Double

    /**
     * The [Match]es found during the comparison.
     */
    val matches: Sequence<M>
}
