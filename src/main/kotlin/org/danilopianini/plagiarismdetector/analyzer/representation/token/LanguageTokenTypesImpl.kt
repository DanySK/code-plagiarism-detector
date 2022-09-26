package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a [LanguageTokenTypes].
 * @param tokensTypes the set of token types of the language
 */
class LanguageTokenTypesImpl(private val tokensTypes: Collection<TokenType>) : LanguageTokenTypes {

    override fun tokenFor(constructName: String): TokenType =
        tokensTypes.find { constructName in it.languageConstructs } ?: error("No token found for given construct name.")

    override fun isToken(constructName: String): Boolean =
        tokensTypes.any { constructName in it.languageConstructs }
}
