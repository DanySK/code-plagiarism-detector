package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a lexical [Token].
 */g
data class TokenImpl(
    override val line: Int,
    override val column: Int,
    override val type: TokenType
) : Token {
    override fun toString(): String {
        return "${type.name} (line=$line, column=$column)"
    }
}
