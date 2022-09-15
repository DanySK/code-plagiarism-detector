package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * An interface modeling a token type.
 */
interface TokenType {
    /**
     * The name of the type.
     */
    val identifier: String

    /**
     * The language types represented by this type.
     */
    val types: Collection<String>
}
