package repository

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import provider.BitbucketProvider
import provider.GitHubProvider
import java.net.URL

private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
private const val EXPECTED_SOURCES = 8

class RepositoryTest : FunSpec({
    val githubRepo = GitHubProvider().byLink(URL(GH_SAMPLE_REPO_URL))
    val bitbucketRepo = BitbucketProvider().byLink(URL(BB_SAMPLE_REPO_URL))

    test("Check if returns all sources") {
        githubRepo.getSources("java").count() shouldBeExactly EXPECTED_SOURCES
        bitbucketRepo.getSources("java").count() shouldBeExactly EXPECTED_SOURCES
    }

    test("Trying to get sources of unknown language should throw an exception") {
        shouldThrow<java.lang.IllegalArgumentException> {
            githubRepo.getSources("non-existing-language")
        }
        shouldThrow<java.lang.IllegalArgumentException> {
            bitbucketRepo.getSources("non-existing-language")
        }
    }
})
