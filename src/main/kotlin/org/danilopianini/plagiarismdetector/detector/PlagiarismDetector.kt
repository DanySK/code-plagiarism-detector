package org.danilopianini.plagiarismdetector.detector

import org.danilopianini.plagiarismdetector.analyzer.representation.SourceRepresentation

/**
 * An interface modeling a detector of similarities between two [SourceRepresentation].
 */
interface PlagiarismDetector<in S : SourceRepresentation<T>, T, out M : Match> :
    (Pair<S, S>) -> (ComparisonResult<M>)

/**
 * An interface modeling two sections of code that are similar.
 */
interface Match

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
