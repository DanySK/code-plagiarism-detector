package org.danilopianini.plagiarismdetector.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.input.cli.technique.TokenizationConfig
import org.danilopianini.plagiarismdetector.repository.Repository
import java.io.File

class TokenizationFacadeTest : FunSpec() {

    init {
        test("Testing file exclusion from detection process") {
            val tokenizationConfig = mockk<TokenizationConfig> {
                every { language } returns Java
                every { filterThreshold } returns null
                every { minimumTokens } returns 15
            }
            val tokenization = TokenizationFacade(tokenizationConfig)
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
    }

    private fun loadFiles(vararg fileName: String): Sequence<File> =
        fileName.asSequence().map { File(ClassLoader.getSystemResource(it).toURI()) }
}
