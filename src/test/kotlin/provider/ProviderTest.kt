package provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContainDuplicates
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldMatch
import provider.criteria.ByBitbucketName
import provider.criteria.ByBitbucketUser
import provider.criteria.ByGitHubName
import provider.criteria.ByGitHubUser
import repository.Repository
import java.net.URL

private const val GH_URL_PREFIX = "https://github.com"
private const val BB_URL_PREFIX = "https://bitbucket.org"
private const val USERNAME = "Danilo Pianini"
private const val GH_USER = "DanySK"
private const val BB_USER = "danysk"

class ProviderTest : FunSpec() {
    init {
        val githubProvider = GitHubProvider()
        val bitbucketProvider = BitbucketProvider()

        test("Searching by existing name and user should return repos matching those criteria") {
            var searchedRepoName = "Student-Project-OOP"
            testByExistingName(
                githubProvider.byCriteria(ByGitHubName(searchedRepoName, ByGitHubUser(GH_USER))),
                searchedRepoName,
                USERNAME
            )
            searchedRepoName = "OOP"
            testByExistingName(
                bitbucketProvider.byCriteria(ByBitbucketName(searchedRepoName, ByBitbucketUser(BB_USER))),
                searchedRepoName,
                USERNAME
            )
        }

        test("Searching by existing URL should return the repo pointed to") {
            var expectedRepoUrl = "$GH_URL_PREFIX/$GH_USER/code-plagiarism-detector"
            testByExistingUrl(
                githubProvider.byLink(URL(expectedRepoUrl)),
                "code-plagiarism-detector",
                USERNAME
            )
            expectedRepoUrl = "$BB_URL_PREFIX/$BB_USER/courses-oop-gradle-jfx-template"
            testByExistingUrl(
                bitbucketProvider.byLink(URL(expectedRepoUrl)),
                "Courses - OOP - Gradle JFX template",
                USERNAME
            )
        }

        test("Searching by *non-existing* URL should return null") {
            githubProvider.byLink(URL("$GH_URL_PREFIX/$GH_USER/non-existing-repo")).shouldBeNull()
            bitbucketProvider.byLink(URL("$BB_URL_PREFIX/$BB_USER/non-existing-repo")).shouldBeNull()
        }

        test("Searching by illegal URL should throw an exception") {
            shouldThrow<java.lang.IllegalArgumentException> {
                githubProvider.byLink(URL("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                githubProvider.byLink(URL("$GH_URL_PREFIX/$GH_USER/code-plagiarism-detector/tree/master/src/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                bitbucketProvider.byLink(URL("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                bitbucketProvider.byLink(URL("$BB_URL_PREFIX/$BB_USER/courses-oop-gradle-jfx-template/src/"))
            }
        }

        test("Searching by a *non-existing* name should return an empty collection of repos") {
            githubProvider.byCriteria(
                ByGitHubName("non-existing-repo", ByGitHubUser(GH_USER))
            ).shouldBeEmpty()
            bitbucketProvider.byCriteria(
                ByBitbucketName("non-existing-repo", ByBitbucketUser(GH_USER))
            ).shouldBeEmpty()
        }

        test("Searching by a *non-existing* user should return an empty collection of repos") {
            githubProvider.byCriteria(
                ByGitHubUser("non-existing-user")
            ).shouldBeEmpty()
            bitbucketProvider.byCriteria(
                ByBitbucketUser("non-existing-user")
            ).shouldBeEmpty()
        }
    }

    private fun testByExistingName(result: Iterable<Repository>, expectedName: String, expectedUser: String) {
        result.shouldNotBeEmpty()
        result.shouldNotContainDuplicates()
        result.forEach {
            it.name.startsWith(expectedName, ignoreCase = true)
            it.owner.shouldMatch(expectedUser)
        }
    }

    private fun testByExistingUrl(result: Repository?, expectedName: String, expectedUser: String) {
        result.shouldNotBeNull()
        result.name.shouldMatch(expectedName)
        result.owner.shouldMatch(expectedUser)
    }
}
