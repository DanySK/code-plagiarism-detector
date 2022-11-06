package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.BadParameterValue
import com.github.ajalt.clikt.core.MissingOption
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.danilopianini.plagiarismdetector.input.cli.provider.CorpusProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.ProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.SubmissionProviderCommand
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubName
import java.net.URL

class ProviderCommandTest : FunSpec() {

    init {
        test("No arguments to provider subcommands should throw exception") {
            shouldThrow<IllegalArgumentException> {
                SubmissionProviderCommand().parse(emptyList())
                CorpusProviderCommand().parse(emptyList())
            }
        }

        test("No valid url should throw exception") {
            val args = listOf("--url", "wrong-url")
            shouldThrow<BadParameterValue> {
                parsedCommands(args)
            }
        }

        test("No valid service should throw exception") {
            val args = listOf("--service", "wrong-service", "--repository-name", "repo", "--user", "user")
            shouldThrow<BadParameterValue> {
                parsedCommands(args)
            }
        }

        test("Criteria options be all validated") {
            val args = listOf("--service", "github")
            shouldThrow<MissingOption> {
                parsedCommands(args)
            }
        }

        test("Testing provider commands with an url") {
            val urlString = "https://test1.com"
            val args = listOf("--url", urlString)
            parsedCommands(args) {
                it.url.shouldNotBeNull()
                it.url!! shouldBe listOf(URL(urlString))
                it.searchCriteria.shouldBeNull()
            }
        }

        test("Testing provider commands with more than one url") {
            val urls = listOf("https://test1.com", "https://test2.com")
            val args = listOf("--url", urls.joinToString(","))
            parsedCommands(args) {
                it.url.shouldNotBeNull()
                it.url!! shouldBe urls.map(::URL)
                it.searchCriteria.shouldBeNull()
            }
        }

        test("Testing provider commands with a criteria") {
            val args = listOf("--repository-name", "repo1", "--user", "user1", "--service", "github")
            parsedCommands(args) {
                it.url.shouldBeNull()
                it.searchCriteria.shouldNotBeNull()
                it.searchCriteria!!.count() shouldBe 1
                it.searchCriteria!!.first().shouldBeTypeOf<ByGitHubName>()
            }
        }

        test("Testing provider commands with more than one criteria") {
            val users = listOf("user1", "user2")
            val repositories = listOf("repo1", "repo2")
            val services = listOf("github", "bitbucket")
            val args = listOf(
                "--repository-name",
                repositories.joinToString(","),
                "--user",
                users.joinToString(","),
                "--service",
                services.joinToString(",")
            )
            parsedCommands(args) {
                it.url.shouldBeNull()
                it.searchCriteria.shouldNotBeNull()
                it.searchCriteria!!.count() shouldBe (users.count() * repositories.count() * services.count())
            }
        }

        test("Testing provider commands with url and criteria") {
            val args = listOf(
                "--repository-name",
                "repo1",
                "--user",
                "danysk",
                "--service",
                "github",
                "--url",
                "https://test.com"
            )
            parsedCommands(args) {
                it.url.shouldNotBeNull()
                it.url shouldBe listOf(URL("https://test.com"))
                it.searchCriteria.shouldNotBeNull()
                it.searchCriteria!!.count() shouldBe 1
                it.searchCriteria!!.first().shouldBeTypeOf<ByGitHubName>()
            }
        }
    }

    private fun parsedCommands(args: List<String>, actions: (ProviderCommand) -> Unit = { }) {
        with(SubmissionProviderCommand()) {
            parse(args)
            actions(this)
        }
        with(CorpusProviderCommand()) {
            parse(args)
            actions(this)
        }
    }
}
