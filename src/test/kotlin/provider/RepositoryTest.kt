package provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import repository.clonedReposDirectoryPath
import java.io.File
import java.net.URL

private const val GH_SAMPLE_REPO_URL = "https://github.com/APICe-at-DISI/sample-javafx-project"
private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/danysk/courses-oop-gradle-jfx-template"
private const val EXPECTED_GH_REPO_SOURCES = 9
private const val EXPECTED_BB_REPO_SOURCES = 3

class RepositoryTest : FunSpec({
    val githubRepo = GitHubProvider().byLink(URL(GH_SAMPLE_REPO_URL))!!
    val bitbucketRepo = BitbucketProvider().byLink(URL(BB_SAMPLE_REPO_URL))!!

    test("Check if returns all sources") {
        githubRepo.getSources("java").count() shouldBeExactly EXPECTED_GH_REPO_SOURCES
        bitbucketRepo.getSources("java").count() shouldBeExactly EXPECTED_BB_REPO_SOURCES
    }

    test("Trying to get sources of unknown language should throw an exception") {
        shouldThrow<java.lang.IllegalArgumentException> {
            githubRepo.getSources("non-existing-language")
        }
        shouldThrow<java.lang.IllegalArgumentException> {
            bitbucketRepo.getSources("non-existing-language")
        }
    }

    afterSpec {
        File(clonedReposDirectoryPath).deleteRecursively()
    }
})
