package org.danilopianini.plagiarismdetector.analyzer.representation.token.languages

import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenType

/**
 * An interface which models a container of [TokenType]s for a specific language.
 */
interface LanguageTokenTypes {
    /**
     * @return the [TokenType] associated to the given construct.
     */
    fun getTokenTypeBy(constructName: String): TokenType

    /**
     * @return true if the given construct name is valid, false otherwise.
     */
    fun isValidToken(constructName: String): Boolean
}
