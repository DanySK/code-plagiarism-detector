package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token

/**
 * The advanced algorithm proposed in [this paper](https://bit.ly/3f3qzED).
 */
class RKRGreedyStringTiling(
    minimumMatchLength: Int = DEFAULT_MINIMUM_MATCH_LEN
) : BaseGreedyStringTiling(minimumMatchLength) {
    companion object {
        private const val DEFAULT_MINIMUM_MATCH_LEN = 8
    }
    private var searchLen = minimumMatchLength
    private val hashTable = mutableMapOf<Int, MutableSet<Sequence<Token>>>()

    override fun runAlgorithm(pattern: TokenizedSource, text: TokenizedSource) {
        var stop = false
        while (!stop) {
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

    private fun scanPhase(
        tokens: Sequence<Token>,
        ifBranch: (Sequence<Token>) -> (Unit),
        elseBranch: (Sequence<Token>) -> (Unit)
    ) {
        tokens.filter(::isUnmarked).forEach { t ->
            val subTokens = tokens.dropWhile { it != t }
            val distanceToNextTile = distanceToNextTile(subTokens)
            if (distanceToNextTile < searchLen) {
                if (tokens.indexOf(t) + distanceToNextTile != tokens.count()) {
                    val unmarkedTokensAfterTile = tokensFromFirstUnmarked(tokens.drop(distanceToNextTile))
                    ifBranch(unmarkedTokensAfterTile)
                }
            } else {
                elseBranch(subTokens)
            }
        }
    }

    private fun firstPhase(pattern: TokenizedSource, text: TokenizedSource) {
        scanPhase(text.representation, {
            firstPhase(pattern, TokenizedSourceImpl(text.sourceFile, it.toList()))
        }) {
            saveHashValueOf(it.take(searchLen))
        }
    }

    private fun secondPhase(pattern: TokenizedSource, text: TokenizedSource) {
        scanPhase(pattern.representation, {
            secondPhase(TokenizedSourceImpl(pattern.sourceFile, it.toList()), text)
        }) {
            val subPattern = it.take(searchLen)
            val hashValue = hashValue(subPattern)
            if (hashValue in hashTable) {
                hashTable[hashValue]?.forEach { subText ->
                    if (subPattern isEqualsTo subText) {
                        val subPatternFromLastChecked = it.drop(searchLen)
                        val subTextFromLastChecked = text.representation.dropWhile { t -> t != subText.first() }
                            .drop(searchLen)
                        val (patternMatches, textMatches) = scan(subPatternFromLastChecked, subTextFromLastChecked)
                        val completePatternMatch = (subPattern + patternMatches).toList()
                        val completeTextMatch = (subText + textMatches).toList()
                        check(completePatternMatch.count() == completeTextMatch.count())
                        val completeMatchLen = completePatternMatch.count()
                        if (completeMatchLen > 2 * searchLen) {
                            searchLen += patternMatches.count()
                            scanPattern(pattern, text)
                        } else {
                            updateMatches(Pair(pattern, completePatternMatch), Pair(text, completeTextMatch))
                        }
                    }
                }
            }
        }
    }

    override fun updateMatches(
        pattern: Pair<TokenizedSource, List<Token>>,
        text: Pair<TokenizedSource, List<Token>>
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
