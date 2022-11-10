package org.danilopianini.plagiarismdetector.caching

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.file.shouldContainFile
import io.kotest.matchers.file.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.repository.Repository
import java.net.URL

class FileKnowledgeBaseManagerTest : FunSpec() {

    private val knowledgeBaseManager = FileKnowledgeBaseManager()
    private val sampleBitbucketRepo = mockk<Repository> {
        every { name } returns "test-repo-cache-simple"
        every { cloneUrl } returns URL(BB_SAMPLE_REPO_URL)
    }
    private val sampleGitHubRepo = mockk<Repository> {
        every { name } returns "test-repo-cache-without-src-root-folder"
        every { cloneUrl } returns URL(GH_SAMPLE_REPO_URL)
    }

    init {
        test("Testing caching sources") {
            knowledgeBaseManager.isCached(sampleBitbucketRepo) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleBitbucketRepo).shouldNotBeEmpty()
            }
            knowledgeBaseManager.save(sampleBitbucketRepo)
            knowledgeBaseManager.isCached(sampleBitbucketRepo) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleBitbucketRepo)
            repoDir.shouldNotBeEmpty()
            repoDir.path.substringAfterLast(System.getProperty("file.separator")) shouldBe sampleBitbucketRepo.name
            repoDir.shouldContainFile("src")
            knowledgeBaseManager.clean(sampleBitbucketRepo)
            knowledgeBaseManager.isCached(sampleBitbucketRepo) shouldBe false
        }

        test("Testing caching project sources with `src` not in the root directory") {
            knowledgeBaseManager.isCached(sampleGitHubRepo) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleGitHubRepo).shouldNotBeEmpty()
            }
            knowledgeBaseManager.save(sampleGitHubRepo)
            knowledgeBaseManager.isCached(sampleGitHubRepo) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleGitHubRepo)
            repoDir.shouldNotBeEmpty()
            repoDir.path.substringAfterLast(System.getProperty("file.separator")) shouldBe sampleGitHubRepo.name
            repoDir.shouldContainFile("app")
            knowledgeBaseManager.clean(sampleGitHubRepo)
            knowledgeBaseManager.isCached(sampleGitHubRepo) shouldBe false
        }

        afterSpec {
            knowledgeBaseManager.clean(sampleBitbucketRepo)
            knowledgeBaseManager.clean(sampleGitHubRepo)
        }
    }

    companion object {
        private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
    }
}
