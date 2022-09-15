package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * An interface modeling a lexical token.
 */
interface Token {
    /**
     * The line position of the token.
     */
    val line: Int

    /**
     * The column position of the token.
     */
    val column: Int

    /**
     * The token type.
     */
    val type: TokenType
}
