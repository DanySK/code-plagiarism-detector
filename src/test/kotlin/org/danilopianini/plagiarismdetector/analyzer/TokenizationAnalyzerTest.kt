package org.danilopianini.plagiarismdetector.analyzer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.slf4j.LoggerFactory
import java.io.File

class TokenizationAnalyzerTest : FunSpec() {
    companion object {
        const val SIMPLE_FILE_NAME = "SimpleTestClass.java"
    }
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val filePath = ClassLoader.getSystemResource(SIMPLE_FILE_NAME)
    private val analyzer = TokenizationAnalyzer()

    init {
        test("Tokenizing source code should return a tokenized representation of source code") {
            val result = analyzer.execute(File(filePath.toURI()))
            result.sourceFile.path shouldBe filePath.path
            result.representation.count() shouldBeGreaterThan 0
            result.representation.forEach { logger.info(it.toString()) }
        }
    }
}
