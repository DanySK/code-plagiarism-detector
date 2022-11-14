package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.detector.SimilarityEstimationStrategy
import kotlin.math.min

/**
 * Strategy to estimate similarities between [TokenizedSource], accordingly to the given [TokenMatch].
 */
fun interface TokenBasedSimilarityStrategy : SimilarityEstimationStrategy<TokenizedSource, Sequence<Token>, TokenMatch>

/**
 * Computes the similarity using maximum similarities' formula.
 */
class MaxSimilarityStrategy : TokenBasedSimilarityStrategy {

    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>
    ): Double = matches.sumOf { it.length }.toDouble() / min(
        representations.first.representation.count(),
        representations.second.representation.count()
    )
}

/**
 * Computes the similarity using maximum similarities' normalization formula.
 */
class NormalizedMaxSimilarityStrategy : TokenBasedSimilarityStrategy {

    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>,
    ): Double {
        val matchedTokens = matches.sumOf { it.length }
        val matchedSubsequences = if (matches.isEmpty()) 0 else matches.count() - 1
        return (matchedTokens - matchedSubsequences).toDouble() / min(
            representations.first.representation.count(),
            representations.second.representation.count()
        )
    }
}

/**
 * Computes the similarity using the average similarities' formula.
 */
class AverageSimilarityStrategy : TokenBasedSimilarityStrategy {

    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>
    ): Double {
        val totalTokens = representations.first.representation.count() + representations.second.representation.count()
        val matchedTokens = matches.sumOf { it.length }
        return (2 * (matchedTokens)).toDouble() / totalTokens
    }
}

/**
 * Computes the similarity using average similarity normalization formula.
 */
class NormalizedAverageSimilarityStrategy : TokenBasedSimilarityStrategy {

    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>,
    ): Double {
        val totalTokens = representations.first.representation.count() + representations.second.representation.count()
        val matchedTokens = matches.sumOf { it.length }
        val matchedSubsequences = if (matches.isEmpty()) 0 else matches.count() - 1
        return (2 * (matchedTokens - matchedSubsequences)).toDouble() / totalTokens
    }
}
