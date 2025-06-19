package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java

import java.io.File
import org.danilopianini.plagiarismdetector.core.analyzer.StepHandler
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.TokenizationAnalyzer
import org.slf4j.LoggerFactory

/**
 * A concrete Java source code [TokenizationAnalyzer].
 */
class JavaTokenizationAnalyzer :
    TokenizationAnalyzer(
        object : StepHandler<File, List<Token>> {
            override operator fun invoke(input: File): List<Token> = try {
                JavaTokenizer()(JavaPreprocessor()(JavaParser()(input)))
            } catch (e: IllegalStateException) {
                LoggerFactory.getLogger(javaClass).error("Skipping ${input.name} due to: ${e.message}")
                emptyList()
            }
        },
    )
