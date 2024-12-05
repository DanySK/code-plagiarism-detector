package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.Analyzer
import org.danilopianini.plagiarismdetector.core.analyzer.StepHandler
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import java.io.File

/**
 * An abstract [Analyzer] which executes source code tokenization.
 */
open class TokenizationAnalyzer(
    private val pipeline: StepHandler<File, List<Token>>,
) : Analyzer<TokenizedSource, Sequence<Token>> {
    override operator fun invoke(input: File): TokenizedSource = TokenizedSourceImpl(input, pipeline(input))
}
