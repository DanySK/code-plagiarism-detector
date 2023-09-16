package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import java.util.TreeMap
import kotlin.math.max
import kotlin.math.min

/**
 * Thread safe implementation of Running-Karp-Rabin Greedy String Tiling.
 * @param minimumMatchLength the minimum matches length under which ignore them.
 */
class RKRGreedyStringTiling(
    minimumMatchLength: Int,
) : BaseGreedyStringTiling(minimumMatchLength) {

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource): Set<TokenMatch> {
        var searchLength = minimumMatchLength
        val tiles = mutableSetOf<TokenMatch>()
        val marked = Pair(mutableSetOf<Token>(), mutableSetOf<Token>())
        while (searchLength != 0) {
            val (matches, lmax) = searchMatches(pattern, text, marked, searchLength)
            markMatches(tiles, marked, matches)
            searchLength = updateSearchLength(lmax)
        }
        return tiles
    }

    private fun updateSearchLength(actualSearchLength: Int): Int {
        return when {
            actualSearchLength > 2 * minimumMatchLength -> actualSearchLength / 2
            actualSearchLength > minimumMatchLength -> minimumMatchLength
            else -> 0
        }
    }

    override fun searchMatches(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int,
    ): Pair<MaximalMatches, Int> {
        val hashTable = firstPhase(text, marked, searchLength)
        return secondPhase(pattern, text, marked, searchLength, hashTable)
    }

    /**
     * Iterates the [text] tokens hashing the subsequences of length
     * [searchLength], returning the resulting [Map].
     */
    private fun firstPhase(text: TokenizedSource, marked: MarkedTokens, searchLength: Int): Map<Int, Set<List<Token>>> {
        val hashTable: MutableMap<Int, MutableSet<List<Token>>> = mutableMapOf()
        phase(text.representation, marked.second, searchLength) { tokens ->
            tokens.subList(0, searchLength).run {
                val hashValue = hashValueOf(this)
                hashTable[hashValue]?.add(this) ?: hashTable.put(hashValue, mutableSetOf(this))
            }
        }
        return hashTable
    }

    /**
     * Iterates the [pattern] looking for matches in [text] of length [searchLength].
     * [Map] is used to skip sequence of tokens with different hash value.
     */
    private fun secondPhase(
        pattern: TokenizedSource,
        text: TokenizedSource,
        marked: MarkedTokens,
        searchLength: Int,
        hashTable: Map<Int, Set<List<Token>>>,
    ): Pair<MaximalMatches, Int> {
        val matches: MutableMap<Int, MutableList<TokenMatch>> = TreeMap()
        val patternTokens = pattern.representation
        val textTokens = text.representation
        phase(patternTokens, marked.first, searchLength) { tokens ->
            val searched = tokens.take(searchLength)
            val hashValue = hashValueOf(searched)
            hashTable[hashValue]
                ?.filter { candidates -> candidates areEqualsTo searched }
                ?.forEach { matching ->
                    val ptnTokensFromLastChecked = patternTokens.drop(patternTokens.indexOf(searched.last()) + 1)
                    val txtTokensFromLastChecked = textTokens.drop(textTokens.indexOf(matching.last()) + 1)
                    val otherMatches = scan(ptnTokensFromLastChecked, txtTokensFromLastChecked, marked)
                    val patternMatches = searched.toList() + otherMatches.first
                    val textMatches = matching.toList() + otherMatches.second
                    val matchLen = patternMatches.count()
                    if (matchLen > 2 * searchLength) {
                        return searchMatches(pattern, text, marked, matchLen)
                    } else {
                        val match = TokenMatchImpl(Pair(pattern, patternMatches), Pair(text, textMatches), matchLen)
                        matches[matchLen]?.add(match) ?: matches.put(matchLen, mutableListOf(match))
                    }
                }
        }
        return Pair(matches, searchLength)
    }

    private inline fun phase(tokens: Tokens, marked: Set<Token>, searchLength: Int, action: (List<Token>) -> (Unit)) {
        val unmarked = tokens.filterNot(marked::contains).toList()
        unmarked.indices.forEach { index ->
            val tokensFromActual = unmarked.subList(index, unmarked.size)
            val distanceToNextTile = distanceToNextTile(tokensFromActual, marked)
            if (distanceToNextTile >= searchLength) {
                action(tokensFromActual)
            }
        }
    }

    /**
     * Computes the hash value for the given sequence of [tokens].
     * Note that the computation of the modulus is avoided by using the implicit modular
     * arithmetic given by the hardware that forgets carries in integer operations.
     */
    private fun hashValueOf(tokens: List<Token>): Int {
        var hashValue = 0
        tokens.forEach {
            hashValue = ((hashValue shl 1) + it.type.hashCode())
        }
        return hashValue
    }

    /**
     * Returns if this sequence is equals to [other] in terms of their types.
     */
    private infix fun List<Token>.areEqualsTo(other: List<Token>): Boolean =
        if (this.count() == other.count()) zip(other).all { it.first.type == it.second.type } else false

    /**
     * Computes the distance between the first of [tokens] and the first unmarked one.
     * If no unmarked tokens are found before the end of the sequence, its length is returned.
     */
    private fun distanceToNextTile(tokens: List<Token>, marked: Set<Token>): Int {
        val result = tokens.indexOfFirst(marked::contains)
        return if (result == -1) tokens.size else result
    }

    /**
     * Marks and add to [tiles] all the unmarked [matches] given in input.
     */
    private fun markMatches(tiles: MutableSet<TokenMatch>, marked: MutableMarkedTokens, matches: MaximalMatches) {
        val myMatches = matches.toMutableMap()
        while (myMatches.isNotEmpty()) {
            val maxMatch = myMatches.keys.max()
            myMatches.getValue(maxMatch).forEach {
                addToTilesOrElse(it, marked, tiles) {
                    with(unmarkedPartOfMatch(it, marked)) {
                        if (length >= minimumMatchLength) {
                            myMatches[length] = myMatches[length]?.plus(this) ?: mutableListOf(this)
                        }
                    }
                }
            }
            myMatches.remove(maxMatch)
        }
    }

    /**
     * Returns a [TokenMatch] which is the unmarked (i.e. not contained in
     * [marked]) part remaining of the [match] given in input.
     */
    private fun unmarkedPartOfMatch(match: TokenMatch, marked: MarkedTokens): TokenMatch {
        val (firstUnmarkedOfPtn, lastUnmarkedOfPtn) = indexesOfUnmarked(match.pattern.second, marked.first)
        val (firstUnmarkedOfTxt, lastUnmarkedOfTxt) = indexesOfUnmarked(match.text.second, marked.second)
        val start = max(firstUnmarkedOfPtn, firstUnmarkedOfTxt)
        val stop = min(lastUnmarkedOfPtn, lastUnmarkedOfTxt)
        return subMatch(start, stop + 1, match)
    }

    /**
     * Returns a [Pair] with, respectively, the initial and final index of unmarked [tokens].
     */
    private fun indexesOfUnmarked(tokens: List<Token>, marked: Set<Token>): Pair<Int, Int> = with(tokens) {
        Pair(indexOfFirst { it !in marked }, indexOfLast { it !in marked })
    }

    /**
     * Returns a new [TokenMatch] created from [match] one containing matches between
     * the specified [fromIndex] (inclusive) and [toIndex] (exclusive).
     */
    private fun subMatch(fromIndex: Int, toIndex: Int, match: TokenMatch): TokenMatch =
        if (fromIndex < 0 || toIndex <= fromIndex) {
            TokenMatchImpl(Pair(match.pattern.first, emptyList()), Pair(match.text.first, emptyList()), 0)
        } else {
            TokenMatchImpl(
                Pair(match.pattern.first, match.pattern.second.subList(fromIndex, toIndex)),
                Pair(match.text.first, match.text.second.subList(fromIndex, toIndex)),
                toIndex - fromIndex,
            )
        }
}
