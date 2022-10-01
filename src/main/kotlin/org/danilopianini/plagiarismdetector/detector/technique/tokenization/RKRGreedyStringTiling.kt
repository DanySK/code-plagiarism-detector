package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.slf4j.LoggerFactory

/**
 * The advanced algorithm proposed in [this paper](https://bit.ly/3f3qzED).
 */
class RKRGreedyStringTiling(
    minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : BaseGreedyStringTiling(minimumMatchLength) {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 5
    }
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var searchLen = minimumMatchLength
    private val hashTable = mutableMapOf<Int, MutableSet<Sequence<Token>>>()

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource) {
        var stop = false
        while (!stop) {
            logger.debug("Search length: $searchLen")
            scanPattern(pattern, text)
            markMatches()
            when {
                searchLen > 2 * minimumMatchLength -> searchLen /= 2
                searchLen > minimumMatchLength -> searchLen = minimumMatchLength
                else -> stop = true
            }
        }
    }

    override fun scanPattern(pattern: TokenizedSource, text: TokenizedSource) {
        firstPhase(pattern, text)
        secondPhase(pattern, text)
    }

    private fun firstPhase(pattern: TokenizedSource, text: TokenizedSource) {
        text.representation.filter(::isUnmarked).forEach { t ->
            val subText = text.representation.dropWhile { it != t }
            val distanceToNextTile = distanceToNextTile(subText)
            if (distanceToNextTile < searchLen) {
                if (text.representation.indexOf(t) + distanceToNextTile == text.representation.count()) {
                    return
                } else {
                    val unmarkedTokensAfterTile = tokensFromFirstUnmarked(text.representation.drop(distanceToNextTile))
                    return firstPhase(pattern, TokenizedSourceImpl(text.sourceFile, unmarkedTokensAfterTile.toList()))
                }
            } else {
                saveHashValueOf(subText.take(searchLen))
            }
        }
    }

    private fun secondPhase(pattern: TokenizedSource, text: TokenizedSource) {
        pattern.representation.filter(::isUnmarked).forEach { t ->
            val subPattern = pattern.representation.dropWhile { it != t }
            val distanceToNextTile = distanceToNextTile(subPattern)
            if (distanceToNextTile < searchLen) {
                if (pattern.representation.indexOf(t) + distanceToNextTile == pattern.representation.count()) {
                    return
                } else {
                    val unmarkedTokensAfterTile = tokensFromFirstUnmarked(
                        pattern.representation.drop(distanceToNextTile)
                    )
                    return secondPhase(TokenizedSourceImpl(pattern.sourceFile, unmarkedTokensAfterTile.toList()), text)
                }
            } else {
                val subPatternToSearch = subPattern.take(searchLen)
                val hashValue = hashValue(subPatternToSearch)
                if (hashValue in hashTable) {
                    findMaxMatch(hashValue, subPatternToSearch, subPattern, text, pattern)
                }
            }
        }
    }

    private fun findMaxMatch(
        hashValue: Int,
        subPatternToSearch: Sequence<Token>,
        subPattern: Sequence<Token>,
        text: TokenizedSource,
        pattern: TokenizedSource
    ) {
        hashTable[hashValue]?.forEach { subText ->
            if (subPatternToSearch isEqualsTo subText) {
                val subPatternFromLastChecked = subPattern.drop(searchLen)
                val subTextFromLastChecked = text.representation.dropWhile { t -> t != subText.first() }.drop(searchLen)
                val (patternMatches, textMatches) = scan(subPatternFromLastChecked, subTextFromLastChecked)
                val completePatternMatch = (subPatternToSearch + patternMatches).toList()
                val completeTextMatch = (subText + textMatches).toList()
                check(completePatternMatch.count() == completeTextMatch.count())
                val completeMatchLen = completePatternMatch.count()
                if (completeMatchLen > 2 * searchLen) {
                    searchLen = completeMatchLen
                    return scanPattern(pattern, text)
                } else {
                    updateMatches(Pair(pattern, completePatternMatch), Pair(text, completeTextMatch))
                }
            }
        }
    }

    override fun updateMatches(
        pattern: Pair<TokenizedSource, List<Token>>,
        text: Pair<TokenizedSource, List<Token>>,
    ) {
        require(pattern.second.count() == text.second.count())
        val matchLength = pattern.second.count()
        val match = TokenMatchImpl(pattern, text, matchLength)
        with(matches) {
            if (containsKey(matchLength)) this[matchLength]?.add(match) else this[matchLength] = mutableListOf(match)
        }
    }

    override fun markMatches() {
        while (matches.isNotEmpty()) {
            val maxMatch = matches.keys.max()
            matches[maxMatch]?.let {
                it.forEach { m ->
                    if (isNotOccluded(m)) {
                        markTokens(m.pattern.second)
                        markTokens(m.text.second)
                        tiles.add(m)
                    }
                }
            }
            matches.remove(maxMatch)
        }
    }

    private fun saveHashValueOf(tokens: Sequence<Token>) {
        val hashValue = hashValue(tokens)
        with(hashValue) {
            if (hashTable.containsKey(this)) hashTable[this]?.add(tokens) else hashTable[this] = mutableSetOf(tokens)
        }
    }

    private fun hashValue(tokens: Sequence<Token>): Int {
        var hashValue = 0
        tokens.forEach { hashValue = ((hashValue shl 1) + it.type.hashCode()) }
        return hashValue
    }

    private infix fun Sequence<Token>.isEqualsTo(target: Sequence<Token>): Boolean {
        if (count() != target.count()) {
            return false
        }
        repeat(count()) {
            if (this.elementAt(it).type != target.elementAt(it).type) {
                return false
            }
        }
        return true
    }

    private fun tokensFromFirstUnmarked(tokens: Sequence<Token>) = tokens.dropWhile(::isMarked)

    private fun distanceToNextTile(tokens: Sequence<Token>): Int {
        val result = tokens.indexOfFirst(::isMarked)
        return if (result == -1) tokens.count() else result
    }
}
