package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a [LanguageTokenTypes].
 * @param tokensTypes the set of token types of the language
 */
class LanguageTokenTypesImpl(private val tokensTypes: Collection<TokenType>) : LanguageTokenTypes {

    override fun tokenFor(constructName: String): TokenType {
        require(isToken(constructName))
        return tokensTypes.find { it.languageConstructs.contains(constructName) }!!
    }

    override fun isToken(constructName: String): Boolean =
        tokensTypes.any { it.languageConstructs.contains(constructName) }
}
