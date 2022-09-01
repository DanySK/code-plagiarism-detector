package org.danilopianini.plagiarismdetector.repository

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.mockk.every
import io.mockk.spyk
import org.json.JSONObject
import org.kohsuke.github.GHRepository
import java.net.URL

class RepositoryTest : FunSpec() {
    companion object {
        private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val EXPECTED_SOURCES = 8
    }

    private val githubRepo = spyk(GitHubRepository(GHRepository())) {
        every { cloneUrl } returns URL(GH_SAMPLE_REPO_URL)
    }
    private val bitbucketRepo = spyk(BitbucketRepository(JSONObject())) {
        every { cloneUrl } returns URL(BB_SAMPLE_REPO_URL)
    }

    init {
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
    }
}
