package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import java.util.TreeMap

/** A map with (hash value, set of sequences with that hash value). */
typealias HashTable = Map<Int, Set<Tokens>>

/**
 * Thread safe implementation of Running-Karp-Rabin Greedy String Tiling.
 * @param minimumMatchLength the minimum matches length under which ignore them.
 */
class RKRGreedyStringTiling(
    minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN,
) : BaseGreedyStringTiling(minimumMatchLength) {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 5
    }

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch> {
        var searchLength = minimumMatchLength
        val tiles = mutableSetOf<TokenMatch>()
        val marked = Pair(mutableSetOf<Token>(), mutableSetOf<Token>())
        while (searchLength != 0) {
            val (matches, lmax) = scanPattern(pattern, text, marked, searchLength)
            matches.toSortedMap(Comparator.reverseOrder()).forEach {
                val (newTiles, newMarked) = mark(marked, matches, it.key)
                tiles.addAll(newTiles)
                marked.addAll(newMarked)
            }
            searchLength = updateSearchLength(lmax)
        }
        return tiles
    }

    private fun updateSearchLength(lmax: Int): Int {
        return when {
            lmax > 2 * minimumMatchLength -> lmax / 2
            lmax > minimumMatchLength -> minimumMatchLength
            else -> 0
        }
    }

    override fun scanPattern(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int,
    ): Pair<MaximalMatches, Int> {
        val hashTable = firstPhase(text, marked, searchLength)
        return secondPhase(pattern, text, marked, searchLength, hashTable)
    }

    private fun firstPhase(text: TokenizedSource, marked: MarkedTokens, searchLength: Int): HashTable {
        val hashTable: MutableMap<Int, MutableSet<Tokens>> = mutableMapOf()
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
        hashTable: HashTable,
    ): Pair<MaximalMatches, Int> {
        val matches: MutableMap<Int, MutableList<TokenMatch>> = TreeMap()
        val patternTokens = pattern.representation
        val textTokens = text.representation
        phase(patternTokens, marked.first, searchLength) { index, tokens ->
            val searchedTokens = tokens.take(searchLength)
            val hashValue = hashValueOf(searchedTokens)
            hashTable[hashValue]?.let {
                it.filter { candidates -> searchedTokens areEqualsTo candidates }
                    .forEach { matchingTokens ->
                        val lastCheckedPatternTokens = patternTokens.drop(index + searchLength)
                        val lastCheckedTextTokens = textTokens.drop(textTokens.indexOf(matchingTokens.last()) + 1)
                        val otherMatches = scan(lastCheckedPatternTokens, lastCheckedTextTokens, marked)
                        val patternMatches = searchedTokens.toList() + otherMatches.first
                        val textMatches = matchingTokens.toList() + otherMatches.second
                        val matchLen = patternMatches.count()
                        if (matchLen > 2 * searchLength) {
                            return scanPattern(pattern, text, marked, matchLen)
                        } else {
                            val match = TokenMatchImpl(Pair(pattern, patternMatches), Pair(text, textMatches), matchLen)
                            matches[matchLen]?.add(match) ?: matches.put(matchLen, mutableListOf(match))
                        }
                    }
            }
        }
        return Pair(matches, searchLength)
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

    private fun hashValueOf(tokens: Tokens): Int {
        var hashValue = 0
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

    private fun distanceToNextTile(tokens: Tokens, marked: Set<Token>): Int {
        val result = tokens.indexOfFirst(marked::contains)
        return if (result == -1) tokens.count() else result
    }
}
