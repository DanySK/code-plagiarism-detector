package org.danilopianini.plagiarismdetector.core.analyzer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeInRange
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import java.io.File

class JavaPreprocessorTest : FunSpec() {

    init {
        test("Test preprocessing phase") {
            val sourceFile = File(ClassLoader.getSystemResource(FILE_NAME).toURI())
            val result = JavaTokenizationAnalyzer()(sourceFile).representation.toList()
            result.shouldNotBeEmpty()
            result.forEach {
                it.line shouldBeInRange IntRange(FIRST_LINE_CLASS, LAST_LINE_CLASS_WITHOUT_EQUALS_AND_HASHCODE)
            }
        }
    }

    companion object {
        private const val FILE_NAME = "TestPreprocessing.java"
        private const val FIRST_LINE_CLASS = 7
        private const val LAST_LINE_CLASS_WITHOUT_EQUALS_AND_HASHCODE = 13
    }
}
