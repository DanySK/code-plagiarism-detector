package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.ComparisonStrategy
import kotlin.math.min

/**
 * Implementation of Greedy String Tiling algorithm.
 * [Here](https://bit.ly/3f3qzED) you can find the paper in which was originally described.
 * @param minimumMatchLength the minimum matches length under which they are ignored.
 */
class GreedyStringTiling(
    private val minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 5
    }
    private var maxMatch = minimumMatchLength
    private val tiles = mutableSetOf<TokenMatch>()
    private val marked = mutableSetOf<Token>()
    private val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()

    override fun invoke(input: Pair<TokenizedSource, TokenizedSource>): Sequence<TokenMatch> {
        clearPreviousResults()
        val (pattern, text) = orderInput(input)
        runAlgorithm(pattern, text)
        return tiles.asSequence()
    }

    private fun clearPreviousResults() {
        tiles.clear()
        marked.clear()
        matches.clear()
    }

    /**
     * Orders the input in terms of number of [Token]s owned by the [TokenizedSource].
     * @param input the pair of [TokenizedSource] to order.
     * @return a pair of [TokenizedSource]: in the first position is put the `pattern`, i.e. the
     * shorter of the two, and in the second position the `text`, i.e. the longer.
     */
    private fun orderInput(input: Pair<TokenizedSource, TokenizedSource>): Pair<TokenizedSource, TokenizedSource> =
        if (input.first.representation.count() > input.second.representation.count()) {
            Pair(input.second, input.first)
        } else {
            Pair(input.first, input.second)
        }

    /**
     * Top level algorithm.
     * @param pattern the [TokenizedSource] with the least number of [Token]s.
     * @param text the [TokenizedSource] with the larger number of [Token]s.
     */
    private fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource) {

        do {
            maxMatch = minimumMatchLength
            scanPattern(pattern, text)
            markMatches()
        } while (maxMatch != minimumMatchLength)
    }

    /**
     * Searches for maximal matches. It corresponds to the `scanpattern()` function of the paper.
     * @param pattern the [TokenizedSource] with the larger number of [Token]s
     * @param text the [TokenizedSource] with the smaller number of [Token]s
     */
    private fun scanPattern(pattern: TokenizedSource, text: TokenizedSource) {
        pattern.representation.dropWhile(::isMarked).forEach { p ->
            text.representation.dropWhile(::isMarked).forEach { t ->
                val subPattern = pattern.representation.dropWhile { it != p }
                val subText = text.representation.dropWhile { it != t }
                val (patternMatches, textMatches) = scan(subPattern, subText)
                updateMatches(Pair(pattern, patternMatches), Pair(text, textMatches))
            }
        }
    }

    /**
     * Search a match starting from first elements of `pattern` and `text`.
     * @return a [Pair] in which are encapsulated the matching [Token]s: in the first
     * position those of the pattern and in the second position those of the text.
     */
    private fun scan(pattern: Sequence<Token>, text: Sequence<Token>): Pair<Sequence<Token>, Sequence<Token>> {
        val (matchingPatternTokens, matchingTextTokens) = (0 until min(pattern.count(), text.count()))
            .asSequence()
            .map { Pair(pattern.elementAt(it), text.elementAt(it)) }
            .takeWhile { it.first.type == it.second.type && isUnmarked(it.first) && isUnmarked(it.second) }
            .unzip()
        return Pair(matchingPatternTokens.asSequence(), matchingTextTokens.asSequence())
    }

    /**
     * Updates the matches.
     * @param pattern a pair consisting of the [TokenizedSource] and the sequence of [Token]s that matches
     * @param text a pair consisting of the [TokenizedSource] and the sequence of [Token]s that matches
     */
    private fun updateMatches(
        pattern: Pair<TokenizedSource, Sequence<Token>>,
        text: Pair<TokenizedSource, Sequence<Token>>
    ) {
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

    /**
     * Marks the matches and creates the tiles.
     */
    private fun markMatches() {
        matches[maxMatch]?.let {
            it.forEach { m ->
                if (!isOccluded(m)) {
                    markTokens(m.pattern.second)
                    markTokens(m.text.second)
                    tiles.add(m)
                }
            }
        }
    }

    /**
     * Checks if the given match is occluded.
     * @param tokenMatch the [TokenMatch] to be tested.
     * @return true if the given match is occluded, i.e. all the matching tokens of both the
     * pattern and the text is marked.
     * Note that, according to the paper, given that smaller matches cannot be created before
     * larger ones, it suffices that only the ends of each sequence of matching tokens be
     * tested for occlusion, rather than the whole sequence.
     */
    private fun isOccluded(tokenMatch: TokenMatch) =
        isMarked(tokenMatch.text.second.last()) && isMarked(tokenMatch.pattern.second.last())

    /**
     * @param token the [Token] to be tested.
     * @return true if the given token has **not** been marked, yet.
     */
    private fun isUnmarked(token: Token) = !isMarked(token)

    /**
     * @param token the [Token] to be tested.
     * @return true if the given token has already been marked.
     */
    private fun isMarked(token: Token) = marked.contains(token)

    /**
     * Marks the tokens.
     * @param tokens the [Token]s to be marked.
     */
    private fun markTokens(tokens: Sequence<Token>) = tokens.forEach { marked.add(it) }
}
