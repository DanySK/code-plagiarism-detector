package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java

import org.danilopianini.plagiarismdetector.analyzer.StepHandler
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.TokenizationAnalyzer

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
