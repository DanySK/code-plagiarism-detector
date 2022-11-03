package org.danilopianini.plagiarismdetector.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.mockk.every
import io.mockk.mockk
import java.net.URL

class RepositoryContentTest : FunSpec() {

    private val githubRepo = GitHubRepository(
        mockk {
            every { htmlUrl } returns URL(GH_SAMPLE_REPO_URL)
        }
    )
    private val bitbucketRepo = BitbucketRepository(
        mockk {
            every {
                getJSONObject(any())
                    .getJSONArray(any())
                    .getJSONObject(any())
                    .getString(any())
            } returns BB_SAMPLE_REPO_URL
        }
    )

    init {
        test("Check if returns all sources") {
            val suffixFileJavaPattern = Regex(".*.java$")
            githubRepo.getSources(suffixFileJavaPattern).count() shouldBeExactly EXPECTED_SOURCES
            bitbucketRepo.getSources(suffixFileJavaPattern).count() shouldBeExactly EXPECTED_SOURCES
        }

        test("No sources meets the given pattern") {
            val suffixFileJavaPattern = Regex(".*.cs")
            githubRepo.getSources(suffixFileJavaPattern).count() shouldBeExactly 0
            bitbucketRepo.getSources(suffixFileJavaPattern).count() shouldBeExactly 0
        }
    }

    companion object {
        private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val EXPECTED_SOURCES = 8
    }
}
