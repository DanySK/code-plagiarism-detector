package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.BadParameterValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.engine.spec.tempfile
import io.kotest.extensions.system.SpecSystemExitListener
import io.kotest.extensions.system.SystemExitException
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.danilopianini.plagiarismdetector.output.Output
import org.danilopianini.plagiarismdetector.provider.BitbucketProvider
import org.danilopianini.plagiarismdetector.provider.GitHubProvider

class CLIConfiguratorTest : FunSpec() {

    override fun listeners() = listOf(SpecSystemExitListener)

    init {
        val output = object : Output {
            override fun startComparison(submissionName: String, totalComparisons: Int) =
                println("Start comparison $submissionName ($totalComparisons)")
            override fun tick() = Unit
            override fun endComparison() = println("Ended")
            override fun logInfo(message: String) = println(message)
        }
        val configurator = spyk(CLIConfigurator(output))
        every { configurator getProperty "github" } propertyType GitHubProvider::class answers {
            GitHubProvider.connectAnonymously()
        }
        every { configurator getProperty "bitbucket" } propertyType BitbucketProvider::class answers {
            BitbucketProvider.connectAnonymously()
        }

        test(
            "Launching CLI configuration with multiple users which are not" +
                " present in both services should not throw an exception"
        ) {
            val args = listOf(
                "--output-dir",
                tempdir().path,
                "submission",
                "--url",
                "https://github.com/DanySK/code-plagiarism-detector",
                "corpus",
                "--repository-name",
                "oop",
                "--user",
                "danysk,unibo-oop-projects",
                "--service",
                "github,bitbucket"
            )
            configurator(args)
        }

        test("If `corpus` or `provider` command is not provided, the process exits with an error message") {
            val args = listOf(
                "--output-dir",
                tempdir().path,
                "submission",
                "--url",
                "https://github.com/DanySK/code-plagiarism-detector"
            )
            val thrown = shouldThrow<SystemExitException> { configurator(args) }
            thrown.exitCode shouldBe 1
        }

        test("If output path is not a directory should throw an exception") {
            val outputFile = tempfile()
            val args = listOf("--o", outputFile.path)
            shouldThrow<BadParameterValue> {
                CLI(mockk()).parse(args)
            }
        }
    }
}
