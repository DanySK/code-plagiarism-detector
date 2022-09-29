package org.danilopianini.plagiarismdetector.core.analyzer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import org.slf4j.LoggerFactory
import java.io.File

class TokenizationAnalyzerTest : FunSpec() {
    companion object {
        private const val SIMPLE_FILE_NAME = "CaveGenerator.java"
    }
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val filePath = ClassLoader.getSystemResource(SIMPLE_FILE_NAME)
    private val analyzer = JavaTokenizationAnalyzer()

    init {
        test("Tokenizing source code should return a tokenized representation of source code") {
            val sourceFile = File(filePath.toURI())
            val result = analyzer(sourceFile)
            result.sourceFile.path shouldBe sourceFile.path
            result.representation.count() shouldBeGreaterThan 0
            result.representation.forEach { logger.info(it.toString()) }
        }
    }
}
