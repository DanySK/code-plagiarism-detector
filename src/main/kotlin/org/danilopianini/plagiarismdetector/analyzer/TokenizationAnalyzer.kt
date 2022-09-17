package org.danilopianini.plagiarismdetector.analyzer

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java.JavaParser
import org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java.JavaPreprocessor
import org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java.JavaTokenizer
import org.danilopianini.plagiarismdetector.analyzer.steps.StepHandler
import java.io.File

/**
 * A concrete [Analyzer] which executes source code tokenization.
 */
class TokenizationAnalyzer : Analyzer<File, TokenizedSource, Sequence<Token>> {
    private val pipeline: StepHandler<File, Sequence<Token>> = StepHandler {
        JavaTokenizer().process(
            JavaPreprocessor().process(
                JavaParser().process(it)
            )
        )
    }

    override fun execute(input: File): TokenizedSource = TokenizedSourceImpl(input, pipeline.process(input))
}
