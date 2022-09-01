package org.danilopianini.plagiarismdetector.repository

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
}
