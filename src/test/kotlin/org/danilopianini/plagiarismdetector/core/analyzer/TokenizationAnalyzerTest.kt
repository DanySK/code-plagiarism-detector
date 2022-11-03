package org.danilopianini.plagiarismdetector.core.analyzer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaParser
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import java.io.File

class TokenizationAnalyzerTest : FunSpec() {

    private val analyzer = JavaTokenizationAnalyzer()

    init {
        test("**Parsing** source file with errors should skip it and print an error message on logger") {
            val sourceFileWithError = tempfile()
            sourceFileWithError.writeText(
                """
                public class ClassWithErrors {
                    public static void main() {
                        System.out.println("Hello World")   // This line does not end with semicolon!
                    }
                }
                """.trimIndent()
            )
            shouldThrow<IllegalStateException> {
                JavaParser()(sourceFileWithError)
            }
        }

        test("Processing phase should remove imports and package declarations, equals and hashcode functions") {
            val sourceFile = File(ClassLoader.getSystemResource(FILE_NAME).toURI())
            val result = analyzer(sourceFile).representation.toList()
            result.shouldNotBeEmpty()
            result.forEach {
                it.line shouldBeInRange IntRange(
                    FIRST_LINE_CLASS,
                    LAST_LINE_CLASS_WITHOUT_EQUALS_AND_HASHCODE
                )
            }
        }

        test("Tokenizing source code should return a tokenized representation of source code") {
            val sourceFile = File(ClassLoader.getSystemResource(FILE_NAME).toURI())
            val result = analyzer(sourceFile)
            result.sourceFile.path shouldBe sourceFile.path
            result.representation.count() shouldBeExactly EXPECTED_TOKENS_NUMBER
        }
    }

    companion object {
        private const val FILE_NAME = "TestAnalyzer.java"
        private const val FIRST_LINE_CLASS = 6
        private const val LAST_LINE_CLASS_WITHOUT_EQUALS_AND_HASHCODE = 16
        private const val EXPECTED_TOKENS_NUMBER = 14
    }
}
