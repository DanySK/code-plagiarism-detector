package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java

import org.danilopianini.plagiarismdetector.core.analyzer.StepHandler
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.TokenizationAnalyzer
import org.slf4j.LoggerFactory
import java.io.File

/**
 * A concrete Java source code [TokenizationAnalyzer].
 */
class JavaTokenizationAnalyzer : TokenizationAnalyzer(
    object : StepHandler<File, List<Token>> {
        override operator fun invoke(input: File): List<Token> = runCatching {
            JavaTokenizer()(
                JavaPreprocessor()(
                    JavaParser()(input)
                )
            )
        }.getOrElse {
            LoggerFactory.getLogger(this.javaClass).error("Skipping ${input.name} due to: ${it.message}")
            emptyList()
        }
    }
)
