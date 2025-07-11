package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource

/**
 * Basic thread-safe implementation of Greedy String Tiling algorithm.
 * @param minimumMatchLength the minimum matches length under which ignore them.
 */
class GreedyStringTiling(minimumMatchLength: Int) : BaseGreedyStringTiling(minimumMatchLength) {
    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch> {
        val tiles = mutableSetOf<TokenMatch>()
        val marked: MutableMarkedTokens = Pair(mutableSetOf(), mutableSetOf())
        val matches: MutableMap<Int, List<TokenMatch>> = mutableMapOf()
        do {
            val (selectedMatches, largestMatch) = searchMatches(pattern, text, marked, minimumMatchLength)
            matches.putAll(selectedMatches)
            matches[largestMatch]?.forEach { addToTilesOrElse(it, marked, tiles) }
        } while (largestMatch != minimumMatchLength)
        return tiles
    }

    override fun searchMatches(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int,
    ): Pair<MaximalMatches, Int> {
        var maxMatch = searchLength
        val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()
        pattern.representation.dropWhile(marked.first::contains).forEach { p ->
            text.representation.dropWhile(marked.second::contains).forEach { t ->
                val patternTokensFromActual = pattern.representation.dropWhile { it != p }
                val textTokensFromActual = text.representation.dropWhile { it != t }
                val (patternMatches, textMatches) = scan(patternTokensFromActual, textTokensFromActual, marked)
                val matchLength = patternMatches.count()
                if (matchLength >= maxMatch) {
                    maxMatch = matchLength
                    val match = TokenMatchImpl(Pair(pattern, patternMatches), Pair(text, textMatches), matchLength)
                    matches[matchLength]?.add(match) ?: matches.put(matchLength, mutableListOf(match))
                }
            }
        }
        return Pair(matches, maxMatch)
    }
}
