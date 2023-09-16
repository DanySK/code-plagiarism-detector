package org.danilopianini.plagiarismdetector.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.file.shouldContainNFiles
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSessionImpl
import org.danilopianini.plagiarismdetector.input.configuration.RunConfigurationImpl
import org.danilopianini.plagiarismdetector.input.configuration.TokenizationConfigurationImpl
import org.danilopianini.plagiarismdetector.output.Output
import org.danilopianini.plagiarismdetector.output.exporter.PlainFileExporter
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.Java
import java.io.File

class SessionTest : FunSpec() {

    init {
        val output = object : Output {
            override fun startComparison(submissionName: String, totalComparisons: Int) =
                println("Start comparison $submissionName ($totalComparisons)")
            override fun tick() = Unit
            override fun endComparison() = println("Ended")
            override fun logInfo(message: String) = println(message)
        }

        test("If no corpus is found to check no file is generated") {
            val temporaryDirectory = tempdir()
            val repo = mockk<GitHubRepository> { every { name } returns "test-repo" }
            val configuration = RunConfigurationImpl(
                mockk<TokenizationFacade>(),
                0.5,
                setOf(repo),
                emptySet(),
                emptySet(),
                PlainFileExporter(temporaryDirectory.toPath(), output),
            )
            AntiPlagiarismSessionImpl(configuration, output)()
            temporaryDirectory shouldContainNFiles 0
        }

        test("Testing tokenization technique session") {
            val tokenizationConfigs = TokenizationConfigurationImpl(
                language = Java,
                filterThreshold = null,
                minimumTokens = 15,
            )
            val submission = setOf<Repository>(
                spyk {
                    every { name } returns "test-submission-1"
                    every { getSources(any()) } returns loadFiles(
                        "CaveGenerator.java",
                        "EditorBoard.java",
                        "TestAnalyzer.java",
                    )
                },
                spyk {
                    every { name } returns "test-submission-2"
                    every { getSources(any()) } returns loadFiles(
                        "ValidatorTest.java",
                        "TestAnalyzer.java",
                    )
                },
            )
            val corpus = setOf<Repository>(
                spyk {
                    every { name } returns "test-corpus-1"
                    every { getSources(any()) } returns loadFiles(
                        "CaveGeneratorImpl.java",
                        "PlayerImplTest.java",
                    )
                },
                spyk {
                    every { name } returns "test-corpus-2"
                    every { getSources(any()) } returns loadFiles(
                        "InventoryControllerImpl.java",
                        "StandardLevelIncreaseStrategyTest.java",
                        "ValidatorTest.java",
                    )
                },
            )
            val temporaryDirectory = tempdir()
            val configuration = RunConfigurationImpl(
                TokenizationFacade(tokenizationConfigs),
                0.3,
                submission,
                corpus,
                emptySet(),
                PlainFileExporter(temporaryDirectory.toPath(), output),
            )
            AntiPlagiarismSessionImpl(configuration, output)()
            temporaryDirectory shouldContainNFiles 2
        }
    }

    private fun loadFiles(vararg fileName: String): Sequence<File> =
        fileName.asSequence().map { File(ClassLoader.getSystemResource(it).toURI()) }
}
