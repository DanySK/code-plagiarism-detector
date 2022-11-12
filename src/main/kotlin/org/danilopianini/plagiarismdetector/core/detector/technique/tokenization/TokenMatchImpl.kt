package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.apache.commons.io.FileUtils
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import java.nio.charset.Charset

/**
 * A simple implementation for a [TokenMatch].
 */
data class TokenMatchImpl(
    override val pattern: Pair<TokenizedSource, List<Token>>,
    override val text: Pair<TokenizedSource, List<Token>>,
    override val length: Int,
) : TokenMatch {

    override val formattedMatch: Pair<String, String> by lazy {
        Pair(
            (listOf("[${pattern.first.sourceFile.path}]") + matchingSectionOf(pattern)).joinToString(separator = "\n"),
            (listOf("[${text.first.sourceFile.path}]") + matchingSectionOf(text)).joinToString(separator = "\n")
        )
    }

    private fun matchingSectionOf(tokens: Pair<TokenizedSource, List<Token>>): List<String> {
        val matchingLines = tokens.second.sortedWith(compareBy({ it.line }, { it.column }))
        return FileUtils.readLines(tokens.first.sourceFile, Charset.defaultCharset())
            .subList(matchingLines.first().line - 1, matchingLines.last().line)
    }

    override fun toString(): String =
        "P=(${pattern.first.sourceFile.name}, ${pattern.second}), T=(${text.first.sourceFile.name}, ${text.second})"
}
