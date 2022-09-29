package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token

/**
 * A simple implementation for a [TokenMatch].
 */
data class TokenMatchImpl(
    override val pattern: Pair<TokenizedSource, Sequence<Token>>,
    override val text: Pair<TokenizedSource, Sequence<Token>>,
    override val length: Int,
) : TokenMatch {

    override fun toString(): String = "P=${pattern.second.toList()}, T=${text.second.toList()}"
}
