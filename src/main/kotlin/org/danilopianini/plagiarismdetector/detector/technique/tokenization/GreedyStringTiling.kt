package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token

/**
 * Implementation of Greedy String Tiling algorithm.
 * [Here](https://bit.ly/3f3qzED) you can find the paper in which was originally described.
 * @param minimumMatchLength the minimum matches length under which they are ignored.
 */
class GreedyStringTiling(
    minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : BaseGreedyStringTiling(minimumMatchLength) {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 5
    }

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch> {
        var maxMatch: Int
        val marked = Pair(mutableSetOf<Token>(), mutableSetOf<Token>())
        val matches: MutableMap<Int, List<TokenMatch>> = mutableMapOf()
        val tiles = mutableSetOf<TokenMatch>()
        do {
            maxMatch = minimumMatchLength
            val (m, mm) = scanPattern(pattern, text, marked, maxMatch)
            maxMatch = m
            matches.putAll(mm)
            val (newTiles, newMarked) = mark(matches, marked, maxMatch)
            tiles.addAll(newTiles)
            marked.first.addAll(newMarked.first)
            marked.second.addAll(newMarked.second)
        } while (maxMatch != minimumMatchLength)
        return tiles
    }

    private fun scanPattern(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        maxMatch: Int
    ): Pair<Int, MaximalMatches> {
        var iterationMaxMatch = maxMatch
        val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()
        pattern.representation.dropWhile(marked.first::contains).forEach { p ->
            text.representation.dropWhile(marked.second::contains).forEach { t ->
                val subPattern = pattern.representation.dropWhile { it !== p }
                val subText = text.representation.dropWhile { it !== t }
                val (patternMatches, textMatches) = scan(subPattern, subText, marked)
                val matchLength = patternMatches.count()
                if (matchLength >= iterationMaxMatch) {
                    iterationMaxMatch = matchLength
                    val match = TokenMatchImpl(Pair(pattern, patternMatches), Pair(text, textMatches), matchLength)
                    matches[matchLength]?.add(match) ?: matches.put(matchLength, mutableListOf(match))
                }
            }
        }
        return Pair(iterationMaxMatch, matches)
    }
}
