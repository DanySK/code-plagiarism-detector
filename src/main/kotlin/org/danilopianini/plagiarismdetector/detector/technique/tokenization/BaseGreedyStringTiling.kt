package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.ComparisonStrategy
import kotlin.math.min

abstract class BaseGreedyStringTiling(
    protected val minimumMatchLength: Int
) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {
    private val marked = mutableSetOf<Token>()
    protected val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()
    protected val tiles = mutableSetOf<TokenMatch>()

    override operator fun invoke(input: Pair<TokenizedSource, TokenizedSource>): Set<TokenMatch> {
        clearPreviousResults()
        val (pattern, text) = orderInput(input)
        runAlgorithm(pattern, text)
        return tiles
    }

    private fun clearPreviousResults() {
        tiles.clear()
        marked.clear()
        matches.clear()
    }

    /**
     * Orders the [input] in terms of [Token]s number owned by the [TokenizedSource] and
     * return a pair of [TokenizedSource]: in the first position the `pattern`, i.e. the
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
     */
    protected abstract fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource)

    /**
     * Searches for maximal matches between [pattern] and [text].
     * It corresponds to the `scanpattern()` function of the paper.
     */
    protected abstract fun scanPattern(pattern: TokenizedSource, text: TokenizedSource)

    protected abstract fun updateMatches(
        pattern: Pair<TokenizedSource, List<Token>>,
        text: Pair<TokenizedSource, List<Token>>,
    )

    /**
     * Marks the matches and creates the tiles. It corresponds to the `markarrays` function of the paper.
     */
    protected abstract fun markMatches()

    /**
     * Search a match between [pattern] and [text] starting at their respective first elements.
     * Tokens match if their type are equals and both have not already been marked.
     * @return a [Pair] in which are encapsulated the matching [Token]s: in the first
     * position those of the pattern and in the second position those of the text.
     */
    protected fun scan(pattern: Sequence<Token>, text: Sequence<Token>): Pair<List<Token>, List<Token>> {
        val (matchingPatternTokens, matchingTextTokens) = (0 until min(pattern.count(), text.count()))
            .asSequence()
            .map { Pair(pattern.elementAt(it), text.elementAt(it)) }
            .takeWhile { it.first.type == it.second.type && isUnmarked(it.first) && isUnmarked(it.second) }
            .unzip()
        return Pair(matchingPatternTokens, matchingTextTokens)
    }

    /**
     * Checks if the given [tokenMatch] is occluded, i.e. all the matching tokens of both the
     * pattern and the text are marked.
     * Note that, according to the paper, given that smaller matches cannot be created before
     * larger ones, it suffices that only the ends of each sequence of matching tokens be
     * tested for occlusion, rather than the whole sequence.
     */
    protected fun isOccluded(tokenMatch: TokenMatch) =
        isMarked(tokenMatch.text.second.last()) && isMarked(tokenMatch.pattern.second.last())

    /**
     * Returns if the given [token] has **not** been marked, yet.
     */
    protected fun isUnmarked(token: Token) = !isMarked(token)

    /**
     * Returns if the given [token] has already been marked.
     */
    protected fun isMarked(token: Token) = marked.contains(token)

    /**
     * Marks the given [tokens].
     */
    protected fun markTokens(tokens: List<Token>) = tokens.forEach { marked.add(it) }
}
