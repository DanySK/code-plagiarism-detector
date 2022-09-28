package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java

import org.danilopianini.plagiarismdetector.analyzer.StepHandler
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.TokenizationAnalyzer
import java.io.File

/**
 * A concrete Java source code [TokenizationAnalyzer].
 */
class JavaTokenizationAnalyzer : TokenizationAnalyzer(
    object : StepHandler<File, List<Token>> {
        override operator fun invoke(input: File): List<Token> = JavaTokenizer()(
            JavaPreprocessor()(
                JavaParser()(input)
            )
        )
    }
)
