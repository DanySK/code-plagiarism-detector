package org.danilopianini.plagiarismdetector.core.detector

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.mpp.timeInMillis
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.GreedyStringTiling
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.RKRGreedyStringTiling
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenBasedPlagiarismDetector
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatch
import org.slf4j.LoggerFactory
import java.io.File

class TokenBasedPlagiarismDetectorTest : FunSpec() {
    companion object {
        private const val DEFAULT_MIN_TOKEN_LEN = 15
        private const val CAVE_FILE_NAME = "CaveGenerator.java"
        private const val CAVE_PLAGIARIZED_FILE_NAME = "CaveGeneratorImpl.java"
        private const val INVENTORY_FILE_NAME = "InventoryControllerImpl.java"
        private const val EDITOR_FILE_NAME = "EditorBoard.java"
        private const val PLAYER_FILE_NAME = "PlayerImplTest.java"
        private const val VALIDATOR_FILE_NAME = "ValidatorTest.java"
        private const val LEVEL_STRATEGY_FILE = "StandardLevelIncreaseStrategyTest.java"
    }
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val analyzer = JavaTokenizationAnalyzer()

    init {
        test("Testing tokenization detection between similar sources") {
            val file1 = File(ClassLoader.getSystemResource(CAVE_FILE_NAME).toURI())
            val file2 = File(ClassLoader.getSystemResource(CAVE_PLAGIARIZED_FILE_NAME).toURI())
            runDetection(Pair(file1, file2))
        }

        test("Testing tokenization detection between non-similar sources") {
            val file1 = File(ClassLoader.getSystemResource(CAVE_FILE_NAME).toURI())
            val file2 = File(ClassLoader.getSystemResource(INVENTORY_FILE_NAME).toURI())
            runDetection(Pair(file1, file2))
        }

        test("Testing tokenization detection between non-similar sources/2") {
            val file1 = File(ClassLoader.getSystemResource(PLAYER_FILE_NAME).toURI())
            val file2 = File(ClassLoader.getSystemResource(EDITOR_FILE_NAME).toURI())
            runDetection(Pair(file1, file2))
        }

        test("Testing tokenization detection between non-similar sources/3") {
            val file1 = File(ClassLoader.getSystemResource(VALIDATOR_FILE_NAME).toURI())
            val file2 = File(ClassLoader.getSystemResource(LEVEL_STRATEGY_FILE).toURI())
            runDetection(Pair(file1, file2))
        }
    }

    private fun runDetection(files: Pair<File, File>, printStats: Boolean = false) {
        val (GSTElapsedTime, GSTResult) = detect(
            TokenBasedPlagiarismDetector(GreedyStringTiling(DEFAULT_MIN_TOKEN_LEN)),
            files,
        )
        val (RKRElapsedTime, RKRResult) = detect(
            TokenBasedPlagiarismDetector(RKRGreedyStringTiling(DEFAULT_MIN_TOKEN_LEN)),
            files,
        )
        if (printStats) {
            printStats("Greedy String Tiling", GSTElapsedTime, GSTResult)
            printStats("Running-Karp-Rabin Greedy String Tiling", RKRElapsedTime, RKRResult)
        }
        GSTResult.similarity shouldBeExactly RKRResult.similarity
        GSTResult.matches.toSet() shouldContainExactly RKRResult.matches.toSet()
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
        logger.info(">> Score of similarity: ${result.similarity}")
        logger.debug(">> Matches: ${result.matches.map(TokenMatch::toString).forEach(logger::info)}")
    }
}
