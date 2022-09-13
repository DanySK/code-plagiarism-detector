package org.danilopianini.plagiarismdetector.analyzer.representation

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
     * The token identifier.
     */
    val type: Int
}
