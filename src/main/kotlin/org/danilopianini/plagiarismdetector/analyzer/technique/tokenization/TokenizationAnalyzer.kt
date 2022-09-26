package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.Analyzer
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.analyzer.StepHandler
import java.io.File

/**
 * An abstract [Analyzer] which executes source code tokenization.
 */
abstract class TokenizationAnalyzer(
    private val pipeline: StepHandler<File, List<Token>>,
) : Analyzer<File, TokenizedSource, Sequence<Token>> {
    override fun invoke(input: File): TokenizedSource = TokenizedSourceImpl(input, pipeline(input))
}
