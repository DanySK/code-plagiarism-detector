package org.danilopianini.plagiarismdetector.core.analyzer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeInRange
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import java.io.File

class JavaPreprocessorTest : FunSpec() {

    init {
        test("Test preprocessing phase") {
            val sourceFile = File(ClassLoader.getSystemResource(FILE_NAME).toURI())
            JavaTokenizationAnalyzer()(sourceFile).representation
                .forEach { it.line shouldBeInRange IntRange(7, 13) }
        }
    }

    companion object {
        private const val FILE_NAME = "TestPreprocessing.java"
    }
}
