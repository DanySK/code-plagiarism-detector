package org.danilopianini.plagiarismdetector.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.input.configuration.TokenizationConfigurationImpl
import org.danilopianini.plagiarismdetector.repository.Repository
import java.io.File

class TokenizationFacadeTest : FunSpec() {

    init {
        val tokenization = TokenizationFacade(
            TokenizationConfigurationImpl(
                language = Java,
                filterThreshold = null,
                minimumTokens = 15
            )
        )

        test("Testing file exclusion from detection process") {
            val submittedProject = mockk<Repository> {
                every { name } returns "test-submission"
                every { getSources(any()) } returns loadFiles("EditorBoard.java")
            }
            val comparedProject = mockk<Repository> {
                every { name } returns "test-corpus"
                every { getSources(any()) } returns loadFiles("EditorBoard.java")
            }
            val result = tokenization.execute(
                submittedProject,
                comparedProject,
                setOf("EditorBoard.java"),
                0.3
            )
            result.submittedProject shouldBe submittedProject
            result.comparedProject shouldBe comparedProject
            result.comparisonResult.shouldBeEmpty()
        }

        test("Testing detection results") {
            val submittedSourceNames = listOf("CaveGenerator.java", "PlayerImplTest.java")
            val comparedSourceName = "CaveGeneratorImpl.java"
            val submittedProject = mockk<Repository> {
                every { name } returns "cave-generator"
                every { getSources(any()) } returns loadFiles(*submittedSourceNames.toTypedArray())
            }
            val comparedProject = mockk<Repository> {
                every { name } returns "cave-generator-plagiarized"
                every { getSources(any()) } returns loadFiles(comparedSourceName)
            }
            val report = tokenization.execute(submittedProject, comparedProject, emptySet(), 0.3)
            report.submittedProject shouldBe submittedProject
            report.comparedProject shouldBe comparedProject
            report.reportedRatio shouldBe 1
            report.similarity.shouldBe(0.316 plusOrMinus 0.001)
        }
    }

    private fun loadFiles(vararg fileName: String): Sequence<File> =
        fileName.asSequence().map { File(ClassLoader.getSystemResource(it).toURI()) }
}
