package org.danilopianini.plagiarismdetector.provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContainDuplicates
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.matchers.string.shouldMatch
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketName
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketUser
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubName
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUser
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.EnvironmentTokenSupplier
import org.slf4j.LoggerFactory
import java.net.URL

private const val PR_BUILD_VARIABLE = "PR_BUILD"
private const val GH_URL_PREFIX = "https://github.com"
private const val BB_URL_PREFIX = "https://bitbucket.org"
private const val DANYSK_USERNAME = "Danilo Pianini"
private const val DANYSK_GH_USER = "DanySK"
private const val DANYSK_BB_USER = "danysk"
private const val TASSILUCA_USER = "tassiLuca"
private const val TASSILUCA_USERNAME = "Luca Tassinari"
private const val BB_AUTH_USER_VAR = "BB_USER"
private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"

class ProviderTest : FunSpec() {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val githubProvider: GitHubProvider
    private val bitbucketProvider: BitbucketProvider

    init {
        // Since on PR builds secrets are not available, execute providers test with anonymously connections
        if (isExecutingOnPullRequest()) {
            logger.info("Testing providers with anonymous connections.")
            bitbucketProvider = BitbucketProvider.connectAnonymously()
            githubProvider = GitHubProvider.connectAnonymously()
        } else {
            githubProvider = GitHubProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
            bitbucketProvider = BitbucketProvider.connectWithToken(
                EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":")
            )
        }

        test("Searching by existing name and user should return repos matching those criteria") {
            var searchedRepoName = "Project-OOP"
            testByExistingName(
                githubProvider.byCriteria(ByGitHubName(searchedRepoName, ByGitHubUser(DANYSK_GH_USER))),
                searchedRepoName,
                DANYSK_USERNAME
            )
            searchedRepoName = "OOP"
            testByExistingName(
                bitbucketProvider.byCriteria(ByBitbucketName(searchedRepoName, ByBitbucketUser(DANYSK_BB_USER))),
                searchedRepoName,
                DANYSK_USERNAME
            )
        }

        test("Searching by existing URL should return the repo pointed to") {
            val repoName = "test-app-for-code-plagiarism-detector"
            var expectedRepoUrl = "$GH_URL_PREFIX/$TASSILUCA_USER/$repoName"
            testByExistingUrl(
                githubProvider.byLink(URL(expectedRepoUrl)),
                repoName,
                TASSILUCA_USERNAME
            )
            expectedRepoUrl = "$BB_URL_PREFIX/$TASSILUCA_USER/$repoName"
            testByExistingUrl(
                bitbucketProvider.byLink(URL(expectedRepoUrl)),
                repoName,
                TASSILUCA_USERNAME
            )
        }

        test("Searching by *non-existing* URL should throw an exception") {
            shouldThrow<IllegalArgumentException> {
                githubProvider.byLink(URL("$GH_URL_PREFIX/$DANYSK_GH_USER/non-existing-repo"))
            }
            shouldThrow<IllegalArgumentException> {
                bitbucketProvider.byLink(URL("$BB_URL_PREFIX/$DANYSK_BB_USER/non-existing-repo"))
            }
        }

        test("Searching by illegal URL should throw an exception") {
            shouldThrow<java.lang.IllegalArgumentException> {
                githubProvider.byLink(URL("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                githubProvider.byLink(
                    URL("$GH_URL_PREFIX/$DANYSK_GH_USER/code-plagiarism-detector/tree/master/src/")
                )
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                bitbucketProvider.byLink(URL("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                bitbucketProvider.byLink(
                    URL("$BB_URL_PREFIX/$DANYSK_BB_USER/courses-oop-gradle-jfx-template/src/")
                )
            }
        }

        test("Searching by a *non-existing* name should return an empty collection of repos") {
            githubProvider.byCriteria(
                ByGitHubName("non-existing-repo", ByGitHubUser(DANYSK_GH_USER))
            ).shouldBeEmpty()
            bitbucketProvider.byCriteria(
                ByBitbucketName("non-existing-repo", ByBitbucketUser(DANYSK_GH_USER))
            ).shouldBeEmpty()
        }

        test("Searching by a *non-existing* user should throw an exception") {
            shouldThrow<IllegalArgumentException> {
                githubProvider.byCriteria(ByGitHubUser("non-existing-user"))
            }
            shouldThrow<IllegalArgumentException> {
                bitbucketProvider.byCriteria(ByBitbucketUser("non-existing-user"))
            }
        }
    }

    private fun isExecutingOnPullRequest() = System.getenv(PR_BUILD_VARIABLE) == "true"

    private fun testByExistingName(result: Iterable<Repository>, expectedName: String, expectedUser: String) {
        result.shouldNotBeEmpty()
        result.shouldNotContainDuplicates()
        result.forEach {
            it.name shouldContainIgnoringCase expectedName
            it.owner shouldMatch expectedUser
        }
    }

    private fun testByExistingUrl(result: Repository?, expectedName: String, expectedUser: String) {
        result.shouldNotBeNull()
        result.name shouldMatch expectedName
        result.owner shouldMatch expectedUser
    }
}
