package org.danilopianini.plagiarismdetector.provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.matchers.string.shouldMatch
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketName
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketUser
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubNameRest
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUserRest
import org.danilopianini.plagiarismdetector.repository.Repository
import org.slf4j.LoggerFactory
import java.net.URI

class ProviderTest : FunSpec() {
    companion object {
        private const val PR_BUILD_VARIABLE = "PR_BUILD"
        private const val GH_URL_PREFIX = "https://github.com"
        private const val BB_URL_PREFIX = "https://bitbucket.org"
        private const val OOP_PROJECTS_ORGANIZATION = "unibo-oop-projects"
        private const val DANYSK_USER = "danysk"
        private const val TASSILUCA_USER = "tassiLuca"
        private const val BB_AUTH_USER_VAR = "BB_USER"
        private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
    }

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val githubProvider: GitHubRestProvider
    private val bitbucketProvider: BitbucketProvider

    init {
        // Since on PR builds secrets are not available, execute providers test with anonymously connections
        if (isExecutingOnPullRequest()) {
            logger.info("Testing providers with anonymous connections.")
            bitbucketProvider = BitbucketProvider.connectAnonymously()
            githubProvider = GitHubRestProvider.connectAnonymously()
        } else {
            githubProvider = GitHubRestProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
            bitbucketProvider = BitbucketProvider.connectWithToken(
                EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":"),
            )
        }

        test("Searching by existing name and user should return repos matching those criteria") {
            var searchedRepoName = "Project-OOP"
            testByExistingName(
                githubProvider.byCriteria(
                    ByGitHubNameRest(searchedRepoName, ByGitHubUserRest(OOP_PROJECTS_ORGANIZATION)),
                ),
                searchedRepoName,
                OOP_PROJECTS_ORGANIZATION,
            )
            searchedRepoName = "OOP"
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
            ).toSet().shouldBeEmpty()
            bitbucketProvider.byCriteria(
                ByBitbucketName("non-existing-repo", ByBitbucketUser(DANYSK_USER)),
            ).toSet().shouldBeEmpty()
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

    private fun isExecutingOnPullRequest() = System.getenv(PR_BUILD_VARIABLE) == "true"

    private fun testByExistingName(
        result: Sequence<Repository>,
        expectedRepositoryName: String,
        expectedUsername: String,
    ) {
        result.toSet().shouldNotBeEmpty()
        result.forEach {
            it.name shouldContainIgnoringCase expectedRepositoryName
            it.owner shouldMatch expectedUsername
            it.cloneUrl.path shouldContain Regex(
                "^/$expectedUsername/.*$expectedRepositoryName.*",
                RegexOption.IGNORE_CASE,
            )
        }
    }

    private fun testByExistingUrl(result: Repository, expectedName: String, expectedUser: String) {
        result.name shouldMatch expectedName
        result.owner shouldMatch expectedUser
    }
}
