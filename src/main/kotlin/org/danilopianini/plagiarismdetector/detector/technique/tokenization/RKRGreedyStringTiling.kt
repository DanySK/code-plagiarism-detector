package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.ComparisonStrategy
import kotlin.math.min

/**
 * The advanced algorithm proposed in [this paper](https://bit.ly/3f3qzED).
 */
class RKRGreedyStringTiling(
    private val minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 2
    }
    private val tiles = mutableSetOf<TokenMatch>()
    private val marked = mutableSetOf<Token>()
    private val hashTable = mutableMapOf<Int, MutableSet<Sequence<Token>>>()
    private val matches: MutableMap<Int, MutableList<TokenMatch>> = mutableMapOf()

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

    private fun orderInput(input: Pair<TokenizedSource, TokenizedSource>): Pair<TokenizedSource, TokenizedSource> =
        if (input.first.representation.count() > input.second.representation.count()) {
            Pair(input.second, input.first)
        } else {
            Pair(input.first, input.second)
        }

    private fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource) {
        var searchLen = minimumMatchLength
        var stop = false
        while (!stop) {
            val lmax = scanPattern(pattern, text, searchLen) ?: 0
            if (lmax > 2 * searchLen) {
                searchLen = lmax
            } else {
                markMatches()
                when {
                    searchLen > 2 * minimumMatchLength -> searchLen /= 2
                    searchLen > minimumMatchLength -> searchLen = minimumMatchLength
                    else -> stop = true
                }
            }
        }
    }

    private fun scanPattern(pattern: TokenizedSource, text: TokenizedSource, searchLen: Int): Int? {
        firstPhase(pattern, text, searchLen)
        return secondPhase(pattern, text, searchLen)
    }

    private fun firstPhase(pattern: TokenizedSource, text: TokenizedSource, searchLen: Int) {
        text.representation.filter(::isUnmarked).forEach { t ->
            val subText = text.representation.dropWhile { it != t }
            val distanceToNextTile = distanceToNextTile(subText)
            if (distanceToNextTile < searchLen) {
                if (text.representation.indexOf(t) + distanceToNextTile != text.representation.count()) {
                    val subTextFromUnmarkedAfterTile =
                        tokensFromFirstUnmarked(text.representation.drop(distanceToNextTile))
                    firstPhase(
                        pattern,
                        TokenizedSourceImpl(text.sourceFile, subTextFromUnmarkedAfterTile.toList()),
                        searchLen
                    )
                    return
                }
            } else {
                val hashValue = hashValue(subText.take(searchLen))
                with(hashValue) {
                    if (hashTable.containsKey(this)) hashTable[this]!!.add(subText.take(searchLen))
                    else hashTable[this] = mutableSetOf(subText.take(searchLen))
                }
            }
        }
    }

    private fun secondPhase(pattern: TokenizedSource, text: TokenizedSource, searchLen: Int): Int? {
        pattern.representation.filter(::isUnmarked).forEach { p ->
            val subPattern = pattern.representation.dropWhile { it != p }
            val distanceToNextTile = distanceToNextTile(subPattern)
            if (distanceToNextTile < searchLen) {
                if (pattern.representation.indexOf(p) + distanceToNextTile != pattern.representation.count()) {
                    val subPatternFromUnmarkedAfterTile =
                        tokensFromFirstUnmarked(pattern.representation.drop(distanceToNextTile))
                    return secondPhase(
                        TokenizedSourceImpl(pattern.sourceFile, subPatternFromUnmarkedAfterTile.toList()),
                        text,
                        searchLen
                    )
                }
            } else {
                val subsubPattern = subPattern.take(searchLen)
                val hashValue = hashValue(subsubPattern)
                if (hashValue in hashTable) {
                    hashTable[hashValue]?.forEach { subsubText ->
                        if (areEquals(subsubPattern, subsubText)) {
                            val (patternMatches, textMatches) = scan(
                                pattern.representation.dropWhile { it != subsubPattern.last() }.drop(1),
                                text.representation.dropWhile { it != subsubText.last() }.drop(1)
                            )
                            if (patternMatches.count() + searchLen > 2 * searchLen) { // todo simplify
                                return patternMatches.count() + searchLen
                            } else {
                                val ppm = (subsubPattern + patternMatches).toList()
                                val ttm = (subsubText + textMatches).toList()
                                check(ppm.count() == ttm.count())
                                val pm = Pair(pattern, ppm)
                                val tm = Pair(text, ttm)
                                val match = TokenMatchImpl(pm, tm, ppm.count())
                                with(matches) {
                                    if (containsKey(match.length)) {
                                        this[match.length]?.add(match)
                                    } else {
                                        this[match.length] = mutableListOf(match)
                                    }
                                }
                                // println(match)
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun markMatches() {
        while (matches.isNotEmpty()) {
            val maxMatch = matches.keys.max()
            matches[maxMatch]!!.let {
                it.forEach { m ->
                    if (!isOccluded(m)) {
                        markTokens(m.pattern.second)
                        markTokens(m.text.second)
                        tiles.add(m)
                    }
                }
            }
            matches.remove(maxMatch)
        }
    }

    private fun hashValue(tokens: Sequence<Token>): Int {
        var hashValue = 0
        tokens.forEach { hashValue = ((hashValue shl 1) + it.type.hashCode()) }
        return hashValue
    }

    private fun areEquals(pattern: Sequence<Token>, text: Sequence<Token>): Boolean {
        require(pattern.count() == text.count())
        val len = pattern.count()
        repeat(len) {
            if (pattern.elementAt(it).type != text.elementAt(it).type) {
                return false
            }
        }
        return true
    }

    private fun scan(pattern: Sequence<Token>, text: Sequence<Token>): Pair<List<Token>, List<Token>> {
        val (matchingPatternTokens, matchingTextTokens) = (0 until min(pattern.count(), text.count()))
            .asSequence()
            .map { Pair(pattern.elementAt(it), text.elementAt(it)) }
            .takeWhile { it.first.type == it.second.type && isUnmarked(it.first) && isUnmarked(it.second) }
            .unzip()
        return Pair(matchingPatternTokens, matchingTextTokens)
    }

    private fun tokensFromFirstUnmarked(tokens: Sequence<Token>) = tokens.dropWhile(::isMarked)

    private fun distanceToNextTile(tokens: Sequence<Token>): Int {
        val result = tokens.indexOfFirst(::isMarked)
        return if (result == -1) tokens.count() else result
    }

    private fun isOccluded(tokenMatch: TokenMatch) =
        isMarked(tokenMatch.text.second.last()) && isMarked(tokenMatch.pattern.second.last())

    private fun isUnmarked(token: Token) = !isMarked(token)

    private fun isMarked(token: Token) = marked.contains(token)

    private fun markTokens(tokens: List<Token>) = tokens.forEach { marked.add(it) }
}
