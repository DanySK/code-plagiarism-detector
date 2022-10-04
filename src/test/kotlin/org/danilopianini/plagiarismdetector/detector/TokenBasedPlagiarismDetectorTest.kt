package org.danilopianini.plagiarismdetector.detector

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.mpp.timeInMillis
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import org.danilopianini.plagiarismdetector.detector.technique.tokenization.RKRGreedyStringTiling
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
    private val sourceFile = File(ClassLoader.getSystemResource(SOURCE_FILE).toURI())
    private val plagiarizedFile = File(ClassLoader.getSystemResource(PLAGIARIZED_FILE).toURI())
    private val analyzer = JavaTokenizationAnalyzer()

    init {
        test("testing tokenization detection between similar sources") {
            val (GSTElapsedTime, GSTResult) = detect(TokenBasedPlagiarismDetector())
            logger.info("> Greedy String Tiling")
            logger.info(">> Elapsed time: $GSTElapsedTime ms")
            logger.info(">> Score of similarity: ${GSTResult.scoreOfSimilarity}")
            // logger.info(">> Matches: ${GSTResult.matches.map(TokenMatch::toString).forEach(logger::info)}")
            val (RKRElapsedTime, RKRResult) = detect(TokenBasedPlagiarismDetector(RKRGreedyStringTiling()))
            logger.info("> Running-Karp-Rabin Greedy String Tiling")
            logger.info(">> Elapsed time: $RKRElapsedTime ms")
            logger.info(">> Score of similarity: ${RKRResult.scoreOfSimilarity}")
            // logger.info(">> Matches: ${RKRResult.matches.map(TokenMatch::toString).forEach(logger::info)}")
            GSTResult.scoreOfSimilarity shouldBeExactly RKRResult.scoreOfSimilarity
        }
    }

    private fun detect(detector: TokenBasedPlagiarismDetector): Pair<Long, ComparisonResult<TokenMatch>> {
        val start = timeInMillis()
        val result = detector(Pair(analyzer(sourceFile), analyzer(plagiarizedFile)))
        val stop = timeInMillis()
        return Pair(stop - start, result)
    }
}
