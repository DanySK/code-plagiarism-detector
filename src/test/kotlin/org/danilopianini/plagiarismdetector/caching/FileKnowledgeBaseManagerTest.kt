package org.danilopianini.plagiarismdetector.caching

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.file.shouldContainFile
import io.kotest.matchers.file.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.apache.commons.io.FileUtils
import org.danilopianini.plagiarismdetector.repository.Repository
import java.io.File
import java.net.URL
import java.nio.file.Files

class FileKnowledgeBaseManagerTest : FunSpec() {
    private val sampleBitbucketRepo =
        mockk<Repository> {
            every { name } returns "test-repo-cache-simple"
            every { cloneUrl } returns URL(BB_SAMPLE_REPO_URL)
        }
    private val sampleGitHubRepo =
        mockk<Repository> {
            every { name } returns "test-repo-cache-without-src-root-folder"
            every { cloneUrl } returns URL(GH_SAMPLE_REPO_URL)
        }

    init {
        val cacheDir = Files.createTempDirectory("cache-dir").toFile()
        val knowledgeBaseManager = spyk<FileKnowledgeBaseManager>(recordPrivateCalls = true)
        every { knowledgeBaseManager getProperty "repositoryFolder" } propertyType File::class answers { cacheDir }

        test("Testing caching sources") {
            knowledgeBaseManager.isCached(sampleBitbucketRepo) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleBitbucketRepo)
            }
            knowledgeBaseManager.save(sampleBitbucketRepo)
            knowledgeBaseManager.isCached(sampleBitbucketRepo) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleBitbucketRepo)
            repoDir.shouldNotBeEmpty()
            repoDir.path.substringAfterLast(System.getProperty("file.separator")) shouldBe sampleBitbucketRepo.name
            repoDir.shouldContainFile("src")
        }

        test("Testing caching project sources with `src` not in the root directory") {
            knowledgeBaseManager.isCached(sampleGitHubRepo) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleGitHubRepo)
            }
            knowledgeBaseManager.save(sampleGitHubRepo)
            knowledgeBaseManager.isCached(sampleGitHubRepo) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleGitHubRepo)
            repoDir.shouldNotBeEmpty()
            repoDir.path.substringAfterLast(System.getProperty("file.separator")) shouldBe sampleGitHubRepo.name
            repoDir.shouldContainFile("app")
        }

        afterSpec {
            FileUtils.forceDeleteOnExit(cacheDir)
        }
    }

    companion object {
        private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
    }
}
