package org.danilopianini.plagiarismdetector.core.analyzer.representation.token

/**
 * An interface which models a container of [TokenType]s for a specific language.
 */
interface LanguageTokenTypes {
    /**
     * @return the [TokenType] associated to the given construct, or null if the construct has no associated token.
     */
    fun tokenFor(constructName: String): TokenType?
}
