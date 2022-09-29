package org.danilopianini.plagiarismdetector.core.detector

import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation

/**
 * An interface modeling a detector of similarities between two [SourceRepresentation].
 */
interface PlagiarismDetector<in S : SourceRepresentation<T>, T, out M : Match> :
    (Pair<S, S>) -> (ComparisonResult<M>)

/**
 * An interface modeling two sections of code that are similar.
 */
interface Match {

    /**
     * Returns a [Pair] with the formatted matches.
     */
    val formattedMatch: Pair<String, String>
}

/**
 * An interface modeling a comparison result object.
 */
interface ComparisonResult<out M : Match> {

    /**
     * The score of similarity, expressed as a value between 0 and 1.
     */
    val similarity: Double

    /**
     * The [Match]es found during the comparison.
     */
    val matches: Sequence<M>
}
