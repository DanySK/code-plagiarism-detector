package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.ComparisonStrategy
import kotlin.math.min

typealias Tokens = Sequence<Token>

/** In the first position is put the marked tokens of pattern, in the second those of the text. */
typealias MarkedTokens = Pair<Set<Token>, Set<Token>>

/** A map which contains (matchLen, list of token match). */
typealias MaximalMatches = Map<Int, List<TokenMatch>>

/** A map with (hash value, set of sequences with that hash value). */
typealias HashTable = MutableMap<Long, MutableSet<Sequence<Token>>>

/**
 * Thread safe implementation of Running-Karp-Rabin Greedy String Tiling.
 */
class RKRGreedyStringTiling(
    private val minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 5
    }

    override operator fun invoke(input: Pair<TokenizedSource, TokenizedSource>): Set<TokenMatch> {
        val (pattern, text) = orderInput(input)
        return runAlgorithm(pattern, text)
    }

    private fun orderInput(input: Pair<TokenizedSource, TokenizedSource>): Pair<TokenizedSource, TokenizedSource> =
        if (input.first.representation.count() > input.second.representation.count()) {
            Pair(input.second, input.first)
        } else {
            Pair(input.first, input.second)
        }

    private fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch> {
        var stop = false
        var searchLength = minimumMatchLength
        val marked = Pair(mutableSetOf<Token>(), mutableSetOf<Token>())
        val tiles = mutableSetOf<TokenMatch>()
        while (!stop) {
            val (matches, lmax) = scanPattern(pattern, text, marked, searchLength)
            searchLength = lmax
            tiles.addAll(markMatches(marked, matches))
            when {
                searchLength > 2 * minimumMatchLength -> searchLength /= 2
                searchLength > minimumMatchLength -> searchLength = minimumMatchLength
                else -> stop = true
            }
        }
        return tiles
    }

    private fun scanPattern(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int
    ): Pair<MaximalMatches, Int> {
        val hashTable = firstPhase(text, marked, searchLength)
        return secondPhase(pattern, text, marked, searchLength, hashTable)
    }

    private inline fun phase(tokens: Tokens, marked: Set<Token>, searchLength: Int, action: (Int, Tokens) -> (Unit)) {
        tokens.filterNot(marked::contains).forEachIndexed { index, _ ->
            val tokensFromActual = tokens.drop(index)
            val distanceToNextTile = distanceToNextTile(tokensFromActual, marked)
            if (distanceToNextTile > searchLength) {
                action(index, tokensFromActual)
            }
        }
    }

    private fun firstPhase(text: TokenizedSource, marked: MarkedTokens, searchLength: Int): HashTable {
        val hashTable: HashTable = mutableMapOf()
        phase(text.representation, marked.second, searchLength) { _, tokens ->
            tokens.take(searchLength).run {
                val hashValue = hashValueOf(this)
                hashTable[hashValue]?.add(this) ?: hashTable.put(hashValue, mutableSetOf(this))
            }
        }
        return hashTable
    }

    private fun secondPhase(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int,
        hashTable: HashTable
    ): Pair<MaximalMatches, Int> {
        val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()
        val patternTokens = pattern.representation
        val textTokens = text.representation
        phase(patternTokens, marked.first, searchLength) { index, tokens ->
            val searchedTokens = tokens.take(searchLength)
            val hashValue = hashValueOf(searchedTokens)
            hashTable[hashValue]?.let {
                it.filter { searchedTokens areEqualsTo it }.forEach { matchingTokens ->
                    val lastCheckedPatternTokens = patternTokens.drop(index + searchLength)
                    val lastCheckedTextTokens = textTokens.dropWhile { it != matchingTokens.first() }.drop(searchLength)
                    val (patternMatches, textMatches) = completeMatches(
                        Pair(searchedTokens, matchingTokens),
                        Pair(lastCheckedPatternTokens, lastCheckedTextTokens),
                        marked
                    )
                    val matchLength = patternMatches.count()
                    if (matchLength > 2 * searchLength) {
                        return scanPattern(pattern, text, marked, matchLength)
                    } else {
                        val match = matchOf(pattern, text, patternMatches, textMatches)
                        matches[matchLength]?.add(match) ?: matches.put(matchLength, mutableListOf(match))
                    }
                }
            }
        }
        return Pair(matches, searchLength)
    }

    private fun completeMatches(
        checked: Pair<Tokens, Tokens>,
        toCheck: Pair<Tokens, Tokens>,
        marked: MarkedTokens
    ): Pair<Tokens, Tokens> {
        val (patternMatches, textMatches) = scan(toCheck.first, toCheck.second, marked)
        val completePatternMatch = checked.first + patternMatches.asSequence()
        val completeTextMatch = checked.second + textMatches.asSequence()
        return Pair(completePatternMatch, completeTextMatch)
    }

    private fun matchOf(
        pattern: TokenizedSource,
        text: TokenizedSource,
        patternMatches: Tokens,
        textMatches: Tokens
    ): TokenMatch = TokenMatchImpl(
        Pair(pattern, patternMatches.toList()),
        Pair(text, textMatches.toList()),
        patternMatches.count()
    )

    private fun markMatches(
        marked: Pair<MutableSet<Token>, MutableSet<Token>>,
        matches: MaximalMatches
    ): Set<TokenMatch> {
        val tiles = mutableSetOf<TokenMatch>()
        val myMatches = matches.toMutableMap()
        while (myMatches.isNotEmpty()) {
            val maxMatch = myMatches.keys.max()
            myMatches[maxMatch]?.let {
                it.forEach { match ->
                    if (isNotOccluded(match, marked)) {
                        match.pattern.second.forEach(marked.first::add)
                        match.text.second.forEach(marked.second::add)
                        tiles.add(match)
                    }
                }
            }
            myMatches.remove(maxMatch)
        }
        return tiles
    }

    private fun scan(pattern: Tokens, text: Tokens, marked: MarkedTokens): Pair<List<Token>, List<Token>> {
        val (matchingPatternTokens, matchingTextTokens) = (0 until min(pattern.count(), text.count()))
            .asSequence()
            .map { Pair(pattern.elementAt(it), text.elementAt(it)) }
            .takeWhile { it.first.type == it.second.type && areBothUnmarked(it.first, it.second, marked) }
            .unzip()
        return Pair(matchingPatternTokens, matchingTextTokens)
    }

    private fun hashValueOf(tokens: Tokens): Long {
        var hashValue: Long = 0
        tokens.forEach {
            hashValue = ((hashValue shl 1) + it.type.hashCode())
        }
        return hashValue
    }

    private infix fun Tokens.areEqualsTo(others: Tokens): Boolean {
        if (count() != others.count()) {
            return false
        }
        repeat(count()) {
            if (this.elementAt(it).type != others.elementAt(it).type) {
                return false
            }
        }
        return true
    }

    private fun isNotOccluded(tokenMatch: TokenMatch, marked: MarkedTokens) =
        !marked.second.contains(tokenMatch.text.second.last()) &&
            !marked.first.contains(tokenMatch.pattern.second.last())

    private fun distanceToNextTile(tokens: Tokens, marked: Set<Token>): Int {
        val result = tokens.indexOfFirst(marked::contains)
        return if (result == -1) tokens.count() else result
    }

    private fun areBothUnmarked(pattern: Token, text: Token, marked: MarkedTokens) =
        !marked.first.contains(pattern) && !marked.second.contains(text)
}
