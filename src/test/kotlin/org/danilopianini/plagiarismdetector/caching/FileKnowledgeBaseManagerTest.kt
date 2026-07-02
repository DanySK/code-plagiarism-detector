package org.danilopianini.plagiarismdetector.caching

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.file.shouldContainFile
import io.kotest.matchers.file.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import java.io.File
import java.net.URL
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import org.danilopianini.plagiarismdetector.gitRepositoryWith
import org.danilopianini.plagiarismdetector.repository.Repository

class FileKnowledgeBaseManagerTest : FunSpec() {
    private val sampleRepoWithRootSources =
        mockk<Repository> {
            every { name } returns "test-repo-cache-simple"
            every { cloneUrl } returns URL(rootSourcesRepo.toURI().toString())
        }
    private val sampleRepoWithoutRootSources =
        mockk<Repository> {
            every { name } returns "test-repo-cache-without-src-root-folder"
            every { cloneUrl } returns URL(nestedSourcesRepo.toURI().toString())
        }

    init {
        val cacheDir = Files.createTempDirectory("cache-dir").toFile()
        val sharedKnowledgeBase = mockk<SharedKnowledgeBase> {
            every { restore(any(), any()) } returns false
            justRun { store(any(), any()) }
        }
        val knowledgeBaseManager = FileKnowledgeBaseManager(cacheDir, sharedKnowledgeBase)

        test("Testing caching sources") {
            knowledgeBaseManager.isCached(sampleRepoWithRootSources) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleRepoWithRootSources)
            }
            knowledgeBaseManager.save(sampleRepoWithRootSources)
            knowledgeBaseManager.isCached(sampleRepoWithRootSources) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleRepoWithRootSources)
            repoDir.shouldNotBeEmpty()
            repoDir.path.substringAfterLast(System.getProperty("file.separator")) shouldBe
                sampleRepoWithRootSources.name
            repoDir.shouldContainFile("src")
        }

        test("Testing caching project sources with `src` not in the root directory") {
            knowledgeBaseManager.isCached(sampleRepoWithoutRootSources) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleRepoWithoutRootSources)
            }
            knowledgeBaseManager.save(sampleRepoWithoutRootSources)
            knowledgeBaseManager.isCached(sampleRepoWithoutRootSources) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleRepoWithoutRootSources)
            repoDir.shouldNotBeEmpty()
            repoDir.path.substringAfterLast(System.getProperty("file.separator")) shouldBe
                sampleRepoWithoutRootSources.name
            repoDir.shouldContainFile("app")
        }

        afterSpec {
            FileUtils.forceDeleteOnExit(cacheDir)
        }
    }

    companion object {
        private val rootSourcesRepo = gitRepositoryWith("src/Main.java" to "class Main {}")
        private val nestedSourcesRepo = gitRepositoryWith(
            "app/src/Main.java" to "class Main {}",
            "README.md" to "# ignored",
        )
    }
}
