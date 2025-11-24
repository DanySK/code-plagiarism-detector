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
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketName
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketUser
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubNameRest
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUserRest
import org.danilopianini.plagiarismdetector.repository.Repository
import org.slf4j.LoggerFactory

class ProviderTest : FunSpec() {
    companion object {
        private const val GH_URL_PREFIX = "https://github.com"
        private const val BB_URL_PREFIX = "https://bitbucket.org"
        private const val OOP_PROJECTS_ORGANIZATION = "unibo-oop-projects"
        private const val DANYSK_USER = "danysk"
        private const val TASSILUCA_USER = "tassiLuca"
        private const val BB_AUTH_USER_VAR = "BB_USER"
        private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
    }

    private val githubProvider: GitHubRestProvider = when {
        System.getenv(GH_AUTH_TOKEN_VAR).isNullOrBlank() -> GitHubRestProvider.connectAnonymously()
        else -> GitHubRestProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
    }
    private val bitbucketProvider: BitbucketProvider = when {
        System.getenv(BB_AUTH_USER_VAR).isNullOrBlank() || System.getenv(BB_AUTH_TOKEN_VAR).isNullOrBlank() ->
            BitbucketProvider.connectAnonymously()

        else ->
            BitbucketProvider.connectWithToken(
                EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":"),
            )
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
            testByExistingName(
                bitbucketProvider.byCriteria(ByBitbucketName(searchedRepoName, ByBitbucketUser(DANYSK_USER))),
                searchedRepoName,
                DANYSK_USER,
            )
        }

        test("Searching by existing URL should return the repo pointed to") {
            val repoName = "test-app-for-code-plagiarism-detector"
            var expectedRepoUrl = "$GH_URL_PREFIX/$TASSILUCA_USER/$repoName"
            testByExistingUrl(
                githubProvider.byLink(URI(expectedRepoUrl)),
                repoName,
                TASSILUCA_USER,
            )
            expectedRepoUrl = "$BB_URL_PREFIX/$TASSILUCA_USER/$repoName"
            testByExistingUrl(
                bitbucketProvider.byLink(URI(expectedRepoUrl)),
                repoName,
                TASSILUCA_USER,
            )
        }

        test("Searching by *non-existing* URL should throw an exception") {
            shouldThrow<IllegalStateException> {
                githubProvider.byLink(URI("$GH_URL_PREFIX/$DANYSK_USER/non-existing-repo"))
            }
            shouldThrow<IllegalStateException> {
                bitbucketProvider.byLink(URI("$BB_URL_PREFIX/$DANYSK_USER/non-existing-repo"))
            }
        }

        test("Searching by illegal URL should throw an exception") {
            shouldThrow<java.lang.IllegalArgumentException> {
                bitbucketProvider.byLink(URI("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                bitbucketProvider.byLink(
                    URI("$BB_URL_PREFIX/$DANYSK_USER/courses-oop-gradle-jfx-template/src/"),
                )
            }
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
            bitbucketProvider.byCriteria(
                ByBitbucketName("non-existing-repo", ByBitbucketUser(DANYSK_USER)),
            ).shouldBeEmpty()
        }

        test("Searching by a *non-existing* user should throw an exception") {
            shouldThrow<IllegalStateException> {
                githubProvider.byCriteria(ByGitHubUserRest("non-existing-user"))
            }
            shouldThrow<IllegalStateException> {
                bitbucketProvider.byCriteria(ByBitbucketUser("non-existing-user"))
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
