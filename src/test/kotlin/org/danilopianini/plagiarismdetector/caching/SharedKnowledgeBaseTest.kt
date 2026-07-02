package org.danilopianini.plagiarismdetector.caching

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.file.shouldContainFile
import io.kotest.matchers.file.shouldNotBeEmpty
import io.kotest.matchers.file.shouldNotContainFile
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import org.danilopianini.plagiarismdetector.gitRepositoryWith
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git

class SharedKnowledgeBaseTest : FunSpec() {
    init {
        test("restores a project from the shared cache") {
            val remoteCache = bareRepository()
            val (project, source) = projectWithSource()
            SharedKnowledgeBase(
                Files.createTempDirectory("producer").toFile(),
                remoteCache.toURI().toString(),
                getenv = fakeToken,
            ).store(project, source)

            val destination = Files.createTempDirectory("destination").toFile()
            val restored = SharedKnowledgeBase(
                Files.createTempDirectory("consumer").toFile(),
                remoteCache.toURI().toString(),
            ).restore(project, destination)

            restored shouldBe true
            destination.shouldContainFile("src")
            File(destination, "src").shouldContainFile("Main.java")
        }

        test("restores locally once the shared cache has been imported") {
            val remoteCache = bareRepository()
            val (project, source) = projectWithSource()
            val sharedCache = SharedKnowledgeBase(
                Files.createTempDirectory("producer").toFile(),
                remoteCache.toURI().toString(),
                getenv = fakeToken,
            )

            sharedCache.store(project, source)
            FileUtils.deleteDirectory(remoteCache)

            val destination = Files.createTempDirectory("destination").toFile()
            sharedCache.restore(project, destination) shouldBe true

            destination.shouldContainFile("src")
            File(destination, "src").shouldContainFile("Main.java")
        }

        test("stores a cleaned clone in an empty shared cache repository") {
            val remoteCache = bareRepository()
            val original = gitRepositoryWith("src/Main.java" to "class Main {}", "README.md" to "# ignored")
            val project = repository("student", "submission", original.toURI().toURL())
            val localCache = Files.createTempDirectory("local-cache").toFile()

            FileKnowledgeBaseManager(
                repositoryFolder = localCache,
                sharedKnowledgeBase = SharedKnowledgeBase(
                    localCache,
                    remoteCache.toURI().toString(),
                    getenv = fakeToken,
                ),
            ).save(project)

            File(localCache, project.name).shouldContainFile("src")
            File(localCache, project.name).shouldNotContainFile("README.md")
            val restored = Files.createTempDirectory("restored").toFile()
            SharedKnowledgeBase(
                Files.createTempDirectory("consumer").toFile(),
                remoteCache.toURI().toString(),
            ).restore(project, restored) shouldBe true
            restored.shouldContainFile("src")
            restored.shouldNotContainFile("README.md")
        }

        test("does not fail when the shared cache cannot be uploaded") {
            val unavailableCache = Files.createTempDirectory("missing-remote").toFile().also(FileUtils::deleteDirectory)
            val original = gitRepositoryWith("src/Main.java" to "class Main {}")
            val project = repository("student", "submission", original.toURI().toURL())
            val localCache = Files.createTempDirectory("local-cache").toFile()

            FileKnowledgeBaseManager(
                repositoryFolder = localCache,
                sharedKnowledgeBase = SharedKnowledgeBase(
                    localCache,
                    unavailableCache.toURI().toString(),
                    getenv = fakeToken,
                ),
            ).save(project)

            File(localCache, project.name).shouldNotBeEmpty()
            File(localCache, project.name).shouldContainFile("src")
        }

        test("keys cache entries by host owner and repository name") {
            val remoteCache = bareRepository()
            val cacheRoot = Files.createTempDirectory("cache").toFile()
            val sharedCache = SharedKnowledgeBase(cacheRoot, remoteCache.toURI().toString(), getenv = fakeToken)
            val first = repository("owner", "same-name", URI("https://github.com/owner/same-name.git").toURL())
            val second = repository("owner", "same-name", URI("https://example.org/owner/same-name.git").toURL())
            val firstSource = Files.createTempDirectory("first").toFile()
            val secondSource = Files.createTempDirectory("second").toFile()
            File(firstSource, "src").mkdirs()
            File(firstSource, "src/GitHub.java").writeText("class GitHub {}")
            File(secondSource, "src").mkdirs()
            File(secondSource, "src/Example.java").writeText("class Example {}")

            sharedCache.store(first, firstSource)
            sharedCache.store(second, secondSource)
            val firstRestored = Files.createTempDirectory("first-restored").toFile()
            val secondRestored = Files.createTempDirectory("second-restored").toFile()

            sharedCache.restore(first, firstRestored) shouldBe true
            sharedCache.restore(second, secondRestored) shouldBe true
            File(firstRestored, "src").shouldContainFile("GitHub.java")
            File(firstRestored, "src").shouldNotContainFile("Example.java")
            File(secondRestored, "src").shouldContainFile("Example.java")
            File(secondRestored, "src").shouldNotContainFile("GitHub.java")
        }
    }

    private fun bareRepository(): File = Files.createTempDirectory("remote-cache").toFile()
        .also { Git.init().setBare(true).setDirectory(it).call().close() }

    private fun repository(owner: String, name: String, cloneUrl: URL): Repository = mockk {
        every { this@mockk.owner } returns owner
        every { this@mockk.name } returns name
        every { this@mockk.cloneUrl } returns cloneUrl
    }

    private fun projectWithSource(): Pair<Repository, File> {
        val project = repository("student", "submission", URI("https://example.org/student/submission.git").toURL())
        val source = Files.createTempDirectory("source").toFile()
        File(source, "src").mkdirs()
        File(source, "src/Main.java").writeText("class Main {}")
        return project to source
    }

    private companion object {
        private val fakeToken: (String) -> String? = { "token" }
    }
}
