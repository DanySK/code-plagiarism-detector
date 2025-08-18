package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.BadParameterValue
import com.github.ajalt.clikt.core.parse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.engine.spec.tempfile
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.danilopianini.plagiarismdetector.core.TestOutput
import org.danilopianini.plagiarismdetector.provider.BitbucketProvider
import org.danilopianini.plagiarismdetector.provider.GitHubGraphQLProvider
import org.danilopianini.plagiarismdetector.provider.GitHubRestProvider

class CLIConfiguratorTest : FunSpec() {
    init {
        val output = TestOutput
        val configurator = spyk(CLIConfigurator(output))
        every { configurator getProperty "githubRest" } propertyType GitHubGraphQLProvider::class answers {
            GitHubRestProvider.connectAnonymously()
        }
        every { configurator getProperty "githubGraphQL" } propertyType GitHubGraphQLProvider::class answers {
            GitHubGraphQLProvider(null)
        }
        every { configurator getProperty "bitbucket" } propertyType BitbucketProvider::class answers {
            BitbucketProvider.connectAnonymously()
        }

        test("Launching CLI configuration user do not exists should throw exception") {
            val args = listOf(
                "--output-dir",
                tempdir().path,
                "submission",
                "--url",
                "https://github.com/DanySK/code-plagiarism-detector",
                "corpus",
                "--service",
                "github:a-non-existing-user",
            )
            shouldThrow<java.lang.IllegalStateException> { configurator(args) }
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
