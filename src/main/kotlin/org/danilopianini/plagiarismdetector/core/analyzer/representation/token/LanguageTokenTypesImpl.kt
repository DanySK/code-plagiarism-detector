package org.danilopianini.plagiarismdetector.core.analyzer.representation.token

/**
 * A simple class implementing a [LanguageTokenTypes].
 * @param tokensTypes the set of token types of the language
 */
class LanguageTokenTypesImpl(
    private val tokensTypes: Set<TokenType>,
) : LanguageTokenTypes {
    private val reverseLookup =
        tokensTypes
            .flatMap { tokensType -> tokensType.languageConstructs.map { it to tokensType } }
            .toMap()

    override fun tokenFor(constructName: String): TokenType? = reverseLookup[constructName]
}
