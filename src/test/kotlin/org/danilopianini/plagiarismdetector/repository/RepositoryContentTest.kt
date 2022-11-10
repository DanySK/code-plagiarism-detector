package org.danilopianini.plagiarismdetector.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.mockk.every
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.caching.FileKnowledgeBaseManager
import java.net.URL

class RepositoryContentTest : FunSpec() {

    private val githubRepo = GitHubRepository(
        mockk {
            every { htmlUrl } returns URL(GH_SAMPLE_REPO_URL)
            every { name } returns "test-github-repo-content"
        }
    )
    private val bitbucketRepo = BitbucketRepository(
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

        afterSpec {
            FileKnowledgeBaseManager().clean(githubRepo)
            FileKnowledgeBaseManager().clean(bitbucketRepo)
        }
    }

    companion object {
        private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val EXPECTED_SOURCES = 8
    }
}
