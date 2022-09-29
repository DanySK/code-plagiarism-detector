package org.danilopianini.plagiarismdetector.core.detector

import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation

/**
 * This interface encapsulate the strategy used to estimate
 * the similarities between a couple of [SourceRepresentation].
 */
fun interface SimilarityEstimationStrategy<in S : SourceRepresentation<T>, T, in M : Match> {

    /**
     * Estimates the similarity between [representations] accordingly to the given [matches].
     */
    fun similarityOf(representations: Pair<S, S>, matches: Set<M>): Double
}
