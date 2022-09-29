package org.danilopianini.plagiarismdetector.core.analyzer.representation.token

/**
 * An interface modeling a [lexical token](https://en.wikipedia.org/wiki/Lexical_analysis#Token).
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
