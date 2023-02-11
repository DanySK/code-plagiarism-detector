package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.detector.ComparisonStrategy
import kotlin.math.max

class SimpleMatcheTiling(val minimumMatchLength: Int) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {

    private data class Match(val patternOffset: Int, val textOffset: Int, val size: Int)

    private fun <T> List<T>.cutSlice(begin: Int, sliceSize: Int): Pair<List<T>, List<T>> =
        subList(0, begin) to subList(begin+sliceSize, size)

    private fun longestMatch(pattern: List<Token>, text: List<Token>, minLength: Int): Match? {
        var outer = 0
        var bestMatch: Match? = null
        val indicesByMatchType = text.subList(0, max(0, text.size - minLength)).indices.groupBy { text[it].type }
        while (pattern.size - outer > minLength) {
            for (inner in indicesByMatchType[pattern[outer].type].orEmpty()) {
                if (text.size - inner > minLength) {
                    var size = 1
                    while(
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
            outer++
        }
        return bestMatch
    }

    private fun searchSplit(pattern: List<Token>, text: List<Token>, minLength: Int): Set<Match> {
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
                it.size
            )
        }.toSet()
    }
}