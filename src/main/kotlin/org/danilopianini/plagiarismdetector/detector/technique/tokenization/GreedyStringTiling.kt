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
    private var maxMatch = minimumMatchLength

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource) {
        do {
            maxMatch = minimumMatchLength
            scanPattern(pattern, text)
            markMatches()
        } while (maxMatch != minimumMatchLength)
    }

    override fun scanPattern(pattern: TokenizedSource, text: TokenizedSource) {
        pattern.representation.dropWhile(::isMarked).forEach { p ->
            text.representation.dropWhile(::isMarked).forEach { t ->
                val subPattern = pattern.representation.dropWhile { it != p }
                val subText = text.representation.dropWhile { it != t }
                val (patternMatches, textMatches) = scan(subPattern, subText)
                updateMatches(Pair(pattern, patternMatches), Pair(text, textMatches))
            }
        }
    }

    override fun updateMatches(pattern: Pair<TokenizedSource, List<Token>>, text: Pair<TokenizedSource, List<Token>>) {
        require(pattern.second.count() == text.second.count())
        val matchLength = pattern.second.count()
        if (matchLength >= maxMatch) {
            maxMatch = matchLength
            val match = TokenMatchImpl(pattern, text, matchLength)
            with(matches) {
                if (containsKey(maxMatch)) this[maxMatch]?.add(match) else this[maxMatch] = mutableListOf(match)
            }
        }
    }

    override fun markMatches() {
        matches[maxMatch]?.let {
            it.forEach { m ->
                if (isNotOccluded(m)) {
                    markTokens(m.pattern.second)
                    markTokens(m.text.second)
                    tiles.add(m)
                }
            }
        }
    }
}
