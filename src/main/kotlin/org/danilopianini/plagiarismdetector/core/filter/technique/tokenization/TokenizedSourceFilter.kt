package org.danilopianini.plagiarismdetector.core.filter.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenType
import org.danilopianini.plagiarismdetector.core.filter.RepresentationFilter
import org.danilopianini.plagiarismdetector.core.filter.indexer.technique.tokenization.TokenBasedIndexer
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A [RepresentationFilter] for [TokenizedSource].
 */
class TokenizedSourceFilter(
    private val threshold: Double,
) : RepresentationFilter<TokenizedSource, Sequence<Token>> {

    private val indexer = TokenBasedIndexer()

    override operator fun invoke(
        submission: TokenizedSource,
        corpus: Sequence<TokenizedSource>,
    ): Sequence<TokenizedSource> {
        val similarities = corpus
            .associateWith(indexer)
            .mapValues { cosineSimilarityOf(indexer(submission), it.value) }
        val minSimilarity = similarities.values.min()
        val maxSimilarity = similarities.values.max()
        val cutoffValue = minSimilarity + threshold * (maxSimilarity - minSimilarity)
        return similarities.asSequence()
            .filter { it.value >= cutoffValue }
            .map { it.key }
    }

    private fun cosineSimilarityOf(index1: Map<TokenType, Int>, index2: Map<TokenType, Int>): Double =
        index1.keys.sumOf { index2[it]?.times(index1[it] ?: 0) ?: 0 }.div(
            index1.values.norm() * index2.values.norm(),
        )

    private fun Collection<Int>.norm(): Double = sqrt(this.sumOf { it.squared() })

    private fun Int.squared(): Double = this.toDouble().pow(2)
}
