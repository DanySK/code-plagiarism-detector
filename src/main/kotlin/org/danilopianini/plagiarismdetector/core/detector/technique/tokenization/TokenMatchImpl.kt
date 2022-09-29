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
        val patternLines = linesOfMatches(pattern.second)
        val textLines = linesOfMatches(text.second)
        val patternCode = FileUtils.readLines(pattern.first.sourceFile, Charset.defaultCharset())
            .subList(patternLines.first - 1, patternLines.second)
        val textCode = FileUtils.readLines(text.first.sourceFile, Charset.defaultCharset())
            .subList(textLines.first - 1, textLines.second)
        Pair(
            (listOf("[${pattern.first.sourceFile.path}]") + patternCode).joinToString(separator = "\n"),
            (listOf("[${text.first.sourceFile.path}]") + textCode).joinToString(separator = "\n")
        )
    }

    private fun linesOfMatches(matches: List<Token>): Pair<Int, Int> {
        val matchingLines = matches.sortedWith(compareBy({ it.line }, { it.column }))
        return Pair(matchingLines.first().line, matchingLines.last().line)
    }

    override fun toString(): String =
        "P=(${pattern.first.sourceFile.name}, ${pattern.second}), T=(${text.first.sourceFile.name}, ${text.second})"
}
