package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.detector.ComparisonStrategy

/**
 * Simple match tiling algorithm. Scans for the largest tile, then run recursively on the remainder of the tokens.
 * The algorithm finds matches larger than [minimumMatchLength].
 */
class SimpleMatchTiling(
    val minimumMatchLength: Int,
) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {

    private data class Match(val patternOffset: Int, val textOffset: Int, val size: Int)

    private fun <T> List<T>.cutSlice(begin: Int, sliceSize: Int): Pair<List<T>, List<T>> =
        subList(0, begin) to subList(begin + sliceSize, size)

    private fun List<Token>.indicizedByType() = indices.groupBy { this[it].type }

    private fun longestMatch(pattern: List<Token>, text: List<Token>, minLength: Int): Match? {
        var bestMatch: Match? = null
        val textIndicesByMatchType = text.indicizedByType()
        val patternIndicesByMatchType = pattern
            .filter { it.type in textIndicesByMatchType.keys }
            .indicizedByType()
        for ((type, indices) in patternIndicesByMatchType) {
            for (outer in indices) {
                for (inner in textIndicesByMatchType[type].orEmpty()) {
                    var size = 1
                    while (
                        outer + size < pattern.size &&
                        inner + size < text.size &&
                        pattern[outer + size].type == text[inner + size].type
                    ) {
                        size++
                    }
                    if (size > minLength && (bestMatch == null || size > bestMatch.size)) {
                        bestMatch = Match(outer, inner, size)
                    }
                }
            }
        }
        return bestMatch
    }

    private fun searchSplit(pattern: List<Token>, text: List<Token>, minLength: Int): Set<Match> {
        if (pattern.size < minLength || text.size < minLength) {
            return emptySet()
        }
        val longest = longestMatch(pattern, text, minLength)
        val result = mutableSetOf<Match>()
        if (longest != null) {
            result.add(longest)
            val (leftPattern, rightPattern) = pattern.cutSlice(longest.patternOffset, longest.size)
            val (leftText, rightText) = text.cutSlice(longest.textOffset, longest.size)
            val combinations = listOf(
                leftPattern to leftText,
                leftPattern to rightText,
                rightPattern to leftText,
                rightPattern to rightText,
            )
            result.addAll(combinations.flatMap { searchSplit(it.first, it.second, minLength) })
        }
        return result
    }

    override fun invoke(p1: Pair<TokenizedSource, TokenizedSource>): Set<TokenMatch> {
        val (patternPair, textPair) = p1.toList().map { it to it.representation.toList() }.sortedBy { it.second.size }
        val pattern = patternPair.second
        val text = textPair.second
        return searchSplit(pattern, text, minimumMatchLength).map {
            TokenMatchImpl(
                patternPair.first to pattern.subList(it.patternOffset, it.patternOffset + it.size),
                textPair.first to text.subList(it.textOffset, it.textOffset + it.size),
                it.size,
            )
        }.toSet()
    }
}
