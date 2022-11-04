package org.danilopianini.plagiarismdetector.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.file.shouldContainNFiles
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSessionImpl
import org.danilopianini.plagiarismdetector.input.cli.technique.TokenizationConfig
import org.danilopianini.plagiarismdetector.input.configuration.RunConfigurationImpl
import org.danilopianini.plagiarismdetector.output.PlainFileExporter
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import java.io.File

class SessionTest : FunSpec() {

    init {
        test("If no corpus is found to check no file is generated") {
            val temporaryDirectory = tempdir()
            val configuration = RunConfigurationImpl(
                mockk<TokenizationFacade>(),
                0.5,
                setOf(mockk<GitHubRepository>()),
                emptySet(),
                emptySet(),
                PlainFileExporter(temporaryDirectory.toPath())
            )
            AntiPlagiarismSessionImpl(configuration)()
            temporaryDirectory shouldContainNFiles 0
        }

        test("Testing tokenization technique session") {
            val tokenizationConfigs = mockk<TokenizationConfig> {
                every { language } returns Java
                every { minimumTokens } returns 15
                every { filterThreshold } returns null
            }
            val submission = setOf<Repository>(
                spyk {
                    every { name } returns "test-submission-1"
                    every { getSources(any()) } returns loadFiles(
                        "CaveGenerator.java",
                        "EditorBoard.java",
                        "TestAnalyzer.java"
                    )
                },
                spyk {
                    every { name } returns "test-submission-2"
                    every { getSources(any()) } returns loadFiles(
                        "ValidatorTest.java",
                        "TestAnalyzer.java"
                    )
                }
            )
            val corpus = setOf<Repository>(
                spyk {
                    every { name } returns "test-corpus-1"
                    every { getSources(any()) } returns loadFiles(
                        "CaveGeneratorImpl.java",
                        "PlayerImplTest.java"
                    )
                },
                spyk {
                    every { name } returns "test-corpus-2"
                    every { getSources(any()) } returns loadFiles(
                        "InventoryControllerImpl.java",
                        "StandardLevelIncreaseStrategyTest.java",
                        "ValidatorTest.java"
                    )
                }
            )
            val temporaryDirectory = tempdir()
            val configuration = RunConfigurationImpl(
                TokenizationFacade(tokenizationConfigs),
                0.3,
                submission,
                corpus,
                emptySet(),
                PlainFileExporter(temporaryDirectory.toPath())
            )
            AntiPlagiarismSessionImpl(configuration)()
            temporaryDirectory shouldContainNFiles 4
        }
    }

    private fun loadFiles(vararg fileName: String): Sequence<File> =
        fileName.asSequence().map { File(ClassLoader.getSystemResource(it).toURI()) }
}
