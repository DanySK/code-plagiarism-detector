package org.danilopianini.plagiarismdetector.detector

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
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
        private const val NON_PLAGIARIZED_FILE = "NonPlagiarizedClass.java"
    }
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val sourceFile = File(ClassLoader.getSystemResource(SOURCE_FILE).toURI())
    private val plagiarizedFile = File(ClassLoader.getSystemResource(PLAGIARIZED_FILE).toURI())
    private val nonPlagiarizedFile = File(ClassLoader.getSystemResource(NON_PLAGIARIZED_FILE).toURI())
    private val analyzer = JavaTokenizationAnalyzer()

    init {
        test("testing tokenization detection between similar sources") {
            runDetection(Pair(sourceFile, plagiarizedFile))
        }

        test("Testing tokenization detection between non-similar sources") {
            runDetection(Pair(sourceFile, nonPlagiarizedFile))
        }
    }

    private fun runDetection(files: Pair<File, File>, printStats: Boolean = false) {
        val (GSTElapsedTime, GSTResult) = detect(TokenBasedPlagiarismDetector(), files)
        val (RKRElapsedTime, RKRResult) = detect(TokenBasedPlagiarismDetector(RKRGreedyStringTiling()), files)
        if (printStats) {
            printStats("Greedy String Tiling", GSTElapsedTime, GSTResult)
            printStats("Running-Karp-Rabin Greedy String Tiling", RKRElapsedTime, RKRResult)
        }
        GSTResult.scoreOfSimilarity shouldBeExactly RKRResult.scoreOfSimilarity
        GSTResult.matches.toList() shouldContainExactly RKRResult.matches.toList()
    }

    private fun detect(
        detector: TokenBasedPlagiarismDetector,
        filesToCheck: Pair<File, File>,
    ): Pair<Long, ComparisonResult<TokenMatch>> {
        val start = timeInMillis()
        val result = detector(Pair(analyzer(filesToCheck.first), analyzer(filesToCheck.second)))
        val stop = timeInMillis()
        return Pair(stop - start, result)
    }

    private fun printStats(strategyName: String, elapsedTime: Long, result: ComparisonResult<TokenMatch>) {
        logger.info("> $strategyName")
        logger.info(">> Elapsed time: $elapsedTime ms")
        logger.info(">> Score of similarity: ${result.scoreOfSimilarity}")
        logger.debug(">> Matches: ${result.matches.map(TokenMatch::toString).forEach(logger::info)}")
    }
}
