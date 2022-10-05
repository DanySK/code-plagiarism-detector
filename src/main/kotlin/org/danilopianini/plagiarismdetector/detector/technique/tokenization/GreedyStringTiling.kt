package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token

/**
 * Implementation of Greedy String Tiling algorithm.
 * @param minimumMatchLength the minimum matches length under which they are ignored.
 */
class GreedyStringTiling(
    minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : BaseGreedyStringTiling(minimumMatchLength) {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 5
    }

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch> {
        val tiles = mutableSetOf<TokenMatch>()
        val marked = Pair(mutableSetOf<Token>(), mutableSetOf<Token>())
        val matches: MutableMap<Int, List<TokenMatch>> = mutableMapOf()
        do {
            val (selectedMatches, largestMatch) = scanPattern(pattern, text, marked, minimumMatchLength)
            matches.putAll(selectedMatches)
            val (newTiles, newMarked) = mark(marked, matches, largestMatch)
            tiles.addAll(newTiles)
            marked.addAll(newMarked)
        } while (largestMatch != minimumMatchLength)
        return tiles
    }

    override fun scanPattern(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int,
    ): Pair<MaximalMatches, Int> {
        var iterationMaxMatch = searchLength
        val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()
        pattern.representation.dropWhile(marked.first::contains).forEach { p ->
            text.representation.dropWhile(marked.second::contains).forEach { t ->
                val patternTokensFromActual = pattern.representation.dropWhile { it !== p }
                val textTokensFromActual = text.representation.dropWhile { it !== t }
                val (patternMatches, textMatches) = scan(patternTokensFromActual, textTokensFromActual, marked)
                val matchLength = patternMatches.count()
                if (matchLength >= iterationMaxMatch) {
                    iterationMaxMatch = matchLength
                    val match = TokenMatchImpl(Pair(pattern, patternMatches), Pair(text, textMatches), matchLength)
                    matches[matchLength]?.add(match) ?: matches.put(matchLength, mutableListOf(match))
                }
            }
        }
        return Pair(matches, iterationMaxMatch)
    }
}
