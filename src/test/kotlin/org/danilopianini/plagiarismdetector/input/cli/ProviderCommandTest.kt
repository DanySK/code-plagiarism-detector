package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.BadParameterValue
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.parse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.danilopianini.plagiarismdetector.input.cli.provider.CorpusProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.ProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.SubmissionProviderCommand
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUserGraphQL
import java.net.URI

class ProviderCommandTest : FunSpec() {
    init {
        test("No arguments to provider subcommands should throw exception") {
            shouldThrow<PrintMessage> {
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

        test("Criteria options must be `service-name:owner[/repo-query]` formatted") {
            val args = listOf("--service", "illegal-service-query")
            shouldThrow<BadParameterValue> {
                parsedCommands(args)
            }
        }

        test("Testing provider commands with an url") {
            val urlString = "https://test1.com"
            val args = listOf("--url", urlString)
            parsedCommands(args) {
                it.url.shouldNotBeNull() shouldBe listOf(URI(urlString).toURL())
                it.criteria.shouldBeNull()
            }
        }

        test("Testing provider commands with more than one url") {
            val urls = listOf("https://test1.com", "https://test2.com")
            val args = listOf("--url", urls.joinToString(","))
            parsedCommands(args) { argument ->
                argument.url.shouldNotBeNull() shouldBe urls.map(::URI).map { it.toURL() }
                argument.criteria.shouldBeNull()
            }
        }

        test("Testing provider commands with a criteria") {
            val args = listOf("--service", "github:user1/repo1")
            parsedCommands(args) {
                it.url.shouldBeNull()
                val criteria = it.criteria.shouldNotBeNull()
                criteria.count() shouldBe 1
                criteria.first().shouldBeTypeOf<ByGitHubUserGraphQL>()
            }
        }

        test("Criteria options without repository query must work as well") {
            val args = listOf("--service", "github:danysk")
            parsedCommands(args) {
                it.url.shouldBeNull()
                val criteria = it.criteria.shouldNotBeNull()
                criteria.count() shouldBe 1
                criteria.first().shouldBeTypeOf<ByGitHubUserGraphQL>()
            }
        }

        test("Testing provider commands with more than one criteria") {
            val users = listOf("user1", "user2")
            val services = listOf("github", "bitbucket")
            val repoNames = listOf("repo1", "repo2", "repo3")
            val argsList =
                services.flatMap { s ->
                    users.flatMap { u ->
                        repoNames.map {
                            s.plus(":").plus(u).plus("/").plus(it)
                        }
                    }
                }
            val args = listOf("--service", argsList.joinToString(separator = ","))
            parsedCommands(args) {
                it.url.shouldBeNull()
                it.criteria.shouldNotBeNull().count() shouldBe (users.count() * repoNames.count() * services.count())
            }
        }

        test("Testing provider commands with url and criteria") {
            val args =
                listOf(
                    "--service",
                    "github:danysk/repo1",
                    "--url",
                    "https://test.com",
                )
            parsedCommands(args) {
                it.url.shouldNotBeNull() shouldBe listOf(URI("https://test.com").toURL())
                it.criteria.shouldNotBeNull().count() shouldBe 1
                it.criteria!!.first().shouldBeTypeOf<ByGitHubUserGraphQL>()
            }
        }
    }

    private fun parsedCommands(
        args: List<String>,
        actions: (ProviderCommand) -> Unit = { },
    ) {
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
