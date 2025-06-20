package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import kotlin.math.min
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.detector.RepresentationsSimilarityEstimator

/**
 * Strategy to estimate similarities between [TokenizedSource], accordingly to the given [TokenMatch].
 */
fun interface TokenizedSourceSimilarityEstimator :
    RepresentationsSimilarityEstimator<TokenizedSource, Sequence<Token>, TokenMatch>

/**
 * Computes the similarity using the maximum similarity normalization formula,
 * which is computed by summing the number of matched tokens divided by the minimum
 * length of the two representations in terms of tokens number.
 */
class MaxSimilarityEstimator : TokenizedSourceSimilarityEstimator {
    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>,
    ): Double = matches.sumOf { it.length }.toDouble() /
        min(
            representations.first.representation.count(),
            representations.second.representation.count(),
        )
}

/**
 * Computes the similarity using the **penalized** maximum similarity normalization formula.
 * This is computed, according to [ES-Plag paper](https://onlinelibrary.wiley.com/doi/epdf/10.1002/cae.22066),
 * as [MaxSimilarityEstimator] does, but subtracting from the number of matched tokens
 * the number of matched subsequences.
 */
class PenalizedMaxSimilarityEstimator : TokenizedSourceSimilarityEstimator {
    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>,
    ): Double {
        val matchedTokens = matches.sumOf { it.length }
        val matchedSubsequences = if (matches.isEmpty()) 0 else matches.count() - 1
        return (matchedTokens - matchedSubsequences).toDouble() /
            min(
                representations.first.representation.count(),
                representations.second.representation.count(),
            )
    }
}

/**
 * Computes the similarity using the average similarity normalization formula,
 * which is computed summing the number of matched tokens divided by the
 * average length of representations, calculated in terms of tokens number.
 */
class AverageSimilarityEstimator : TokenizedSourceSimilarityEstimator {
    override fun similarityOf(
        representations: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>,
    ): Double {
        val totalTokens = representations.first.representation.count() + representations.second.representation.count()
        val matchedTokens = matches.sumOf { it.length }
        return (2 * (matchedTokens)).toDouble() / totalTokens
    }
}

/**
 * Computes the similarity using the **penalized** average similarity normalization formula.
 * This is computed, according to [ES-Plag paper](https://onlinelibrary.wiley.com/doi/epdf/10.1002/cae.22066),
 * as [AverageSimilarityEstimator] does, but subtracting from the number of matched tokens
 * the number of matched subsequences.
 */
class PenalizedAverageSimilarityEstimator : TokenizedSourceSimilarityEstimator {
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
