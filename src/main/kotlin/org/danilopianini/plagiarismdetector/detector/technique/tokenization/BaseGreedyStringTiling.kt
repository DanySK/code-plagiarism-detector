package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.ComparisonStrategy
import kotlin.math.min

/** A [Sequence] of [Token]s. */
typealias Tokens = Sequence<Token>

/** A [Map] which contains (matchLen, list of token match). */
typealias MaximalMatches = Map<Int, List<TokenMatch>>

/** In the first position is put the marked tokens of pattern, in the second those of the text. */
typealias MarkedTokens = Pair<Set<Token>, Set<Token>>

/**
 * This is an abstract base implementation of the common code for Greedy String Tiling algorithm.
 */
abstract class BaseGreedyStringTiling(
    protected val minimumMatchLength: Int
) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {

    override operator fun invoke(input: Pair<TokenizedSource, TokenizedSource>): Set<TokenMatch> {
        val (pattern, text) = orderInput(input)
        return runAlgorithm(pattern, text)
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
    protected abstract fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch>

    protected fun mark(
        matches: MaximalMatches,
        marked: MarkedTokens,
        maxMatch: Int
    ): Pair<Set<TokenMatch>, MarkedTokens> {
        val tiles = mutableSetOf<TokenMatch>()
        val myMarked = Pair(marked.first.toMutableSet(), marked.second.toMutableSet())
        matches[maxMatch]?.let {
            it.forEach { match ->
                if (isNotOccluded(match, myMarked)) {
                    match.pattern.second.forEach(myMarked.first::add)
                    match.text.second.forEach(myMarked.second::add)
                    tiles.add(match)
                }
            }
        }
        return Pair(tiles, myMarked)
    }

    /**
     * Search a match between [pattern] and [text] starting at their respective first elements.
     * Tokens match if their type are equals and both have not already been marked.
     * @return a [Pair] in which are encapsulated the matching [Token]s: in the first
     * position those of the pattern and in the second position those of the text.
     */
    protected fun scan(pattern: Tokens, text: Tokens, marked: MarkedTokens): Pair<List<Token>, List<Token>> {
        val (matchingPatternTokens, matchingTextTokens) = (0 until min(pattern.count(), text.count()))
            .asSequence()
            .map { Pair(pattern.elementAt(it), text.elementAt(it)) }
            .takeWhile { it.first.type == it.second.type && it.first !in marked.first && it.second !in marked.second }
            .unzip()
        return Pair(matchingPatternTokens, matchingTextTokens)
    }

    /**
     * Checks if the given [tokenMatch] is **not** occluded, i.e. none of all matching tokens of both
     * the pattern and the text has been marked during the creation of an earlier tile.
     * Note that, according to the paper, given that smaller matches cannot be created before
     * larger ones, it suffices that only the ends of each sequence of matching tokens be
     * tested for occlusion, rather than the whole sequence.
     */
    private fun isNotOccluded(tokenMatch: TokenMatch, marked: MarkedTokens) =
        tokenMatch.pattern.second.last() !in marked.first && tokenMatch.text.second.last() !in marked.second

    protected fun Pair<MutableSet<Token>, MutableSet<Token>>.addAll(other: MarkedTokens) {
        this.first.addAll(other.first)
        this.second.addAll(other.second)
    }
}
