package org.danilopianini.plagiarismdetector.analyzer

import org.danilopianini.plagiarismdetector.analyzer.steps.StepHandler
import org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java.JavaParser
import org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java.JavaPreprocessor
import org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java.JavaTokenizer

/**
 * A concrete Java source code [TokenizationAnalyzer].
 */
class JavaTokenizationAnalyzer : TokenizationAnalyzer(
    StepHandler {
        JavaTokenizer().process(
            JavaPreprocessor().process(
                JavaParser().process(it)
            )
        )
    }
)
