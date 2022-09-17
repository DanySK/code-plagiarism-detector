package org.danilopianini.plagiarismdetector.analyzer.representation.token.languages

import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenType

/**
 * A simple class implementing a [LanguageTokenTypes] for Java programming language.
 */
class JavaTokenTypes(private val tokensTypes: Sequence<TokenType>) : LanguageTokenTypes {
    override fun getTokenTypeBy(constructName: String): TokenType {
        require(isValidToken(constructName))
        return tokensTypes.find { it.languageConstructs.contains(constructName) }!!
    }

    override fun isValidToken(constructName: String): Boolean =
        tokensTypes.any { it.languageConstructs.contains(constructName) }
}
