package org.danilopianini.plagiarismdetector.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.mockk.every
import io.mockk.mockk

class RepositoryContentTest : FunSpec() {
    private val githubRepo = GitHubRepository("tassiLuca", "test-app-for-code-plagiarism-detector")

    private val bitbucketRepo =
        BitbucketRepository(
            mockk {
                every {
                    this@mockk.getJSONObject(any())
                        .getJSONArray(any())
                        .getJSONObject(any())
                        .getString(any())
                } returns BB_SAMPLE_REPO_URL
                every {
                    this@mockk.get(any()).toString()
                } returns "test-bitbucket-repo-content"
            },
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
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val EXPECTED_SOURCES = 8
    }
}
