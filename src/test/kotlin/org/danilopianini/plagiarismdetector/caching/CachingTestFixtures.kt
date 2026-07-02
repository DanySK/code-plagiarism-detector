package org.danilopianini.plagiarismdetector.caching

import io.kotest.matchers.file.shouldContainFile
import io.kotest.matchers.file.shouldExist
import io.mockk.every
import io.mockk.mockk
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.file.Files
import org.danilopianini.plagiarismdetector.repository.Repository
import org.eclipse.jgit.api.Git

fun repository(owner: String, name: String, cloneUrl: URL): Repository = mockk {
    every { this@mockk.owner } returns owner
    every { this@mockk.name } returns name
    every { this@mockk.cloneUrl } returns cloneUrl
}

fun bareRepository(): File = Files.createTempDirectory("remote-cache").toFile()
    .also { Git.init().setBare(true).setDirectory(it).call().close() }

fun projectWithSource(): Pair<Repository, File> {
    val project = repository("student", "submission", URI("https://example.org/student/submission.git").toURL())
    val source = Files.createTempDirectory("source").toFile()
    File(source, "src").mkdirs()
    File(source, "src/Main.java").writeText("class Main {}")
    return project to source
}

fun File.cachedProject(project: Repository): File = File(this, project.name)

fun File.shouldContainSource(relativePath: String) {
    val sourceRoot = relativePath.substringBefore('/', relativePath)
    if (sourceRoot != relativePath) {
        shouldContainFile(sourceRoot)
    }
    File(this, relativePath).shouldExist()
}
