package org.danilopianini.plagiarismdetector.caching

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import org.danilopianini.plagiarismdetector.gitRepositoryWith
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.InvalidRemoteException

class FileKnowledgeBaseManagerTest : FunSpec() {
    private val sampleRepoWithRootSources =
        mockk<Repository> {
            every { name } returns "test-repo-cache-simple"
            every { cloneUrl } returns rootSourcesRepo.toURI().toURL()
        }
    private val sampleRepoWithoutRootSources =
        mockk<Repository> {
            every { name } returns "test-repo-cache-without-src-root-folder"
            every { cloneUrl } returns nestedSourcesRepo.toURI().toURL()
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
            repoDir.path.substringAfterLast(FileSystems.getDefault().separator) shouldBe
                sampleRepoWithRootSources.name
            repoDir.shouldContainSource("src/Main.java")
        }

        test("Testing caching project sources with `src` not in the root directory") {
            knowledgeBaseManager.isCached(sampleRepoWithoutRootSources) shouldBe false
            shouldThrow<IllegalArgumentException> {
                knowledgeBaseManager.load(sampleRepoWithoutRootSources)
            }
            knowledgeBaseManager.save(sampleRepoWithoutRootSources)
            knowledgeBaseManager.isCached(sampleRepoWithoutRootSources) shouldBe true
            val repoDir = knowledgeBaseManager.load(sampleRepoWithoutRootSources)
            repoDir.path.substringAfterLast(FileSystems.getDefault().separator) shouldBe
                sampleRepoWithoutRootSources.name
            repoDir.shouldContainSource("app/src/Main.java")
        }

        test("Clone failures are reported") {
            val missingRepository = Files.createTempDirectory(
                "missing-repository",
            ).toFile().also(FileUtils::deleteDirectory)
            val project = mockk<Repository> {
                every { name } returns "missing-repository"
                every { cloneUrl } returns missingRepository.toURI().toURL()
            }
            shouldThrow<InvalidRemoteException> {
                knowledgeBaseManager.save(project)
            }
            knowledgeBaseManager.isCached(project) shouldBe false
        }

        test("temporary git repositories can be cloned with their working tree") {
            val source = gitRepositoryWith("src/Main.java" to "class Main {}")
            val destination = Files.createTempDirectory("clone").toFile()
            Git.cloneRepository()
                .setURI(source.toURI().toString())
                .setDirectory(destination)
                .call()
                .close()
            File(destination, "src/Main.java").shouldExist()
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
