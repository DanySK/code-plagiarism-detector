package provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContainDuplicates
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.string.shouldMatch
import provider.query.ByBitbucketName
import provider.query.ByBitbucketUser
import provider.query.ByGitHubName
import provider.query.ByGitHubUser
import java.net.URL

private const val GH_URL_PREFIX = "https://github.com"
private const val BB_URL_PREFIX = "https://bitbucket.org"
private const val USERNAME = "Danilo Pianini"
private const val GH_USER = "DanySK"
private const val BB_USER = "danysk"

class ProviderTest : FunSpec() {
    init {
        test("Searching by existing name and user should return repos matching that name") {
            var searchedRepoName = "Student-Project-OOP"
            testByExistingName(
                GitHubProvider(ByGitHubName(searchedRepoName, ByGitHubUser(GH_USER))),
                searchedRepoName,
                USERNAME
            )
            searchedRepoName = "OOP"
            testByExistingName(
                BitbucketProvider(ByBitbucketName(searchedRepoName, ByBitbucketUser(BB_USER))),
                searchedRepoName,
                USERNAME
            )
        }

        test("Searching by existing URL should return the repo pointed to") {
            var expectedRepoUrl = "$GH_URL_PREFIX/$GH_USER/code-plagiarism-detector"
            testByExistingUrl(
                GitHubProvider(URL(expectedRepoUrl)),
                "code-plagiarism-detector",
                USERNAME
            )
            expectedRepoUrl = "$BB_URL_PREFIX/$BB_USER/courses-oop-gradle-jfx-template"
            testByExistingUrl(
                BitbucketProvider(URL(expectedRepoUrl)),
                "Courses - OOP - Gradle JFX template",
                USERNAME
            )
        }

        test("Searching by *non-existing* URL should return an empty collection of repos") {
            GitHubProvider(URL("$GH_URL_PREFIX/$GH_USER/non-existing-repo")).repositories.shouldBeEmpty()
            BitbucketProvider(URL("$BB_URL_PREFIX/$BB_USER/non-existing-repo")).repositories.shouldBeEmpty()
        }

        test("Searching by illegal URL should throw an exception") {
            shouldThrow<java.lang.IllegalArgumentException> {
                GitHubProvider(URL("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                GitHubProvider(URL("$GH_URL_PREFIX/$GH_USER/code-plagiarism-detector/tree/master/src/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                BitbucketProvider(URL("https://www.unibo.it/"))
            }
            shouldThrow<java.lang.IllegalArgumentException> {
                BitbucketProvider(URL("$BB_URL_PREFIX/$BB_USER/courses-oop-gradle-jfx-template/src/master/"))
            }
        }

        test("Searching by a *non-existing* name should return an empty collection of repos") {
            GitHubProvider(ByGitHubName("non-existing-repo", ByGitHubUser(GH_USER)))
                .repositories.shouldBeEmpty()
            BitbucketProvider(ByBitbucketName("non-existing-repo", ByBitbucketUser(GH_USER)))
                .repositories.shouldBeEmpty()
        }

        test("Searching by a *non-existing* user should return an empty collection of repos") {
            GitHubProvider(ByGitHubUser("non-existing-user")).repositories.shouldBeEmpty()
            BitbucketProvider(ByBitbucketUser("non-existing-user")).repositories.shouldBeEmpty()
        }
    }

    private fun testByExistingName(
        provider: ProjectsProvider,
        expectedName: String,
        expectedUser: String
    ) {
        val result = provider.repositories
        result.shouldNotBeEmpty()
        result.shouldNotContainDuplicates()
        result.forEach {
            it.name.startsWith(expectedName, ignoreCase = true)
            it.owner.shouldMatch(expectedUser)
        }
    }

    private fun testByExistingUrl(
        provider: ProjectsProvider,
        expectedName: String,
        expectedUser: String
    ) {
        val result = provider.repositories
        result.count().shouldBeExactly(1)
        result.first().name.shouldMatch(expectedName)
        result.first().owner.shouldMatch(expectedUser)
    }
}
