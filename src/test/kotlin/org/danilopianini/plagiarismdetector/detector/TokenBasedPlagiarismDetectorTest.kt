package org.danilopianini.plagiarismdetector.detector

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import org.danilopianini.plagiarismdetector.detector.technique.tokenization.TokenBasedPlagiarismDetector
import org.danilopianini.plagiarismdetector.detector.technique.tokenization.TokenMatch
import org.slf4j.LoggerFactory
import java.io.File

class TokenBasedPlagiarismDetectorTest : FunSpec() {
    companion object {
        private const val SOURCE_FILE = "SimpleTestClass.java"
        private const val PLAGIARIZED_FILE = "SimplePlagiarizedClass.java"
    }
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val fileSourcePath = ClassLoader.getSystemResource(SOURCE_FILE)
    private val filePlagiarizedPath = ClassLoader.getSystemResource(PLAGIARIZED_FILE)
    private val analyzer = JavaTokenizationAnalyzer()

    init {
        test("checking similarity") {
            val sourceFile = File(fileSourcePath.toURI())
            val plagiarizedFile = File(filePlagiarizedPath.toURI())
            val result = TokenBasedPlagiarismDetector().invoke(
                Pair(analyzer(sourceFile), analyzer(plagiarizedFile))
            )
            logger.info("Score of similarity: ${result.scoreOfSimilarity}")
            logger.info("Matches:")
            result.matches.map(TokenMatch::toString).forEach(logger::info)
            result.scoreOfSimilarity shouldBeGreaterThan 0.5
        }
    }
}
