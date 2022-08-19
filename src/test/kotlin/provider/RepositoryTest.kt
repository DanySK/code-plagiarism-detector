package provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import java.net.URL

private const val GH_SAMPLE_REPO_URL = "https://github.com/APICe-at-DISI/sample-javafx-project"
private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/danysk/courses-oop-gradle-jfx-template"

class RepositoryTest : FunSpec({
    val githubProvider = GitHubProvider(URL(GH_SAMPLE_REPO_URL))
    val bitbucketProvider = BitbucketProvider(URL(BB_SAMPLE_REPO_URL))

    test("Check if returns all sources") {
        githubProvider.repositories.first().getSources("Java").count() shouldBeExactly 9
        bitbucketProvider.repositories.first().getSources("Java").count() shouldBeExactly 3
    }

    test("Trying to get sources of unknown language should throw an exception") {
        shouldThrow<java.lang.IllegalArgumentException> {
            githubProvider.repositories.first().getSources("non-existing-language")
        }
        shouldThrow<java.lang.IllegalArgumentException> {
            bitbucketProvider.repositories.first().getSources("non-existing-language")
        }
    }
})
