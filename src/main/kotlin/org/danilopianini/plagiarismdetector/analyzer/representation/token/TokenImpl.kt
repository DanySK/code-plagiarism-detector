package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a lexical [Token].
 */
data class TokenImpl(
    override val line: Int,
    override val column: Int,
    override val type: TokenType
) : Token {
    override fun toString(): String = "${type.name} (line=$line, column=$column)"
}
