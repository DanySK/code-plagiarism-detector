package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token

/**
 * A simple implementation for a [TokenMatch].
 */
data class TokenMatchImpl(
    override val pattern: Pair<TokenizedSource, List<Token>>,
    override val text: Pair<TokenizedSource, List<Token>>,
    override val length: Int,
) : TokenMatch {

    override fun toString(): String =
        "P=(${pattern.first.sourceFile.name}, ${pattern.second}), T=(${text.first.sourceFile.name}, ${text.second})"
}
