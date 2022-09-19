package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * An interface which models a container of [TokenType]s for a specific language.
 */
interface LanguageTokenTypes {
    /**
     * @return the [TokenType] associated to the given construct.
     */
    fun tokenFor(constructName: String): TokenType

    /**
     * @return true if the given construct name is valid, false otherwise.
     */
    fun isToken(constructName: String): Boolean
}
