package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a [LanguageTokenTypes] for Java programming language.
 */
class LanguageTokenTypesImpl(private val tokensTypes: Sequence<TokenType>) : LanguageTokenTypes {
    override fun getTokenTypeBy(constructName: String): TokenType {
        require(isValidToken(constructName))
        return tokensTypes.find { it.languageConstructs.contains(constructName) }!!
    }

    override fun isValidToken(constructName: String): Boolean =
        tokensTypes.any { it.languageConstructs.contains(constructName) }
}
