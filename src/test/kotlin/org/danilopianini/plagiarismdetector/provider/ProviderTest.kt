package org.danilopianini.plagiarismdetector.provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.sequences.shouldBeEmpty
import io.kotest.matchers.sequences.shouldNotBeEmpty
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.shouldMatch
import java.net.URI
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubNameRest
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUserRest
import org.danilopianini.plagiarismdetector.repository.Repository

class ProviderTest : FunSpec() {
    companion object {
        private const val GH_URL_PREFIX = "https://github.com"
        private const val OOP_PROJECTS_ORGANIZATION = "unibo-oop-projects"
        private const val DANYSK_USER = "danysk"
        private const val TASSILUCA_USER = "tassiLuca"
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
    }

    private val githubProvider: GitHubRestProvider = when {
        System.getenv(GH_AUTH_TOKEN_VAR).isNullOrBlank() -> GitHubRestProvider.connectAnonymously()
        else -> GitHubRestProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
    }

    init {
        test("Searching by existing name and user should return repos matching those criteria") {
            val searchedRepoName = "oop"
            testByExistingName(
                githubProvider.byCriteria(
                    ByGitHubNameRest(searchedRepoName, ByGitHubUserRest(OOP_PROJECTS_ORGANIZATION)),
                ),
                searchedRepoName,
                OOP_PROJECTS_ORGANIZATION,
            )
        }

        test("Searching by existing URL should return the repo pointed to") {
            val repoName = "test-app-for-code-plagiarism-detector"
            val expectedRepoUrl = "$GH_URL_PREFIX/$TASSILUCA_USER/$repoName"
            testByExistingUrl(
                githubProvider.byLink(URI(expectedRepoUrl)),
                repoName,
                TASSILUCA_USER,
            )
        }

        test("Searching by *non-existing* URL should throw an exception") {
            shouldThrow<IllegalStateException> {
                githubProvider.byLink(URI("$GH_URL_PREFIX/$DANYSK_USER/non-existing-repo"))
            }
        }

        test("Searching by illegal URL should throw an exception") {
            shouldThrow<java.lang.IllegalArgumentException> {
                githubProvider.byLink(URI("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                githubProvider.byLink(
                    URI("$GH_URL_PREFIX/$DANYSK_USER/code-plagiarism-detector/tree/master/src/"),
                )
            }
        }

        test("Searching by a *non-existing* name should return an empty collection of repos") {
            githubProvider.byCriteria(
                ByGitHubNameRest("non-existing-repo", ByGitHubUserRest(DANYSK_USER)),
            ).shouldBeEmpty()
        }

        test("Searching by a *non-existing* user should throw an exception") {
            shouldThrow<IllegalStateException> {
                githubProvider.byCriteria(ByGitHubUserRest("non-existing-user"))
            }
        }
    }

    private fun testByExistingName(
        result: Sequence<Repository>,
        expectedRepositoryName: String,
        expectedUsername: String,
    ) {
        result.shouldNotBeEmpty()
        result.firstOrNull {
            it.name.contains(expectedRepositoryName, ignoreCase = true) &&
                it.owner == expectedUsername &&
                it.cloneUrl.path.contains(
                    Regex(
                        "^/$expectedUsername/.*$expectedRepositoryName.*",
                        RegexOption.IGNORE_CASE,
                    ),
                )
        } shouldNot beNull()
    }

    private fun testByExistingUrl(result: Repository, expectedName: String, expectedUser: String) {
        result.name shouldMatch expectedName
        result.owner shouldMatch expectedUser
    }
}
