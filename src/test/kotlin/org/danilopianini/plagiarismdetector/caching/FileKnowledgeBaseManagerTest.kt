package org.danilopianini.plagiarismdetector.caching

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.file.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

class FileKnowledgeBaseManagerTest : FunSpec() {

    private val knowledgeBaseManager = FileKnowledgeBaseManager()

    init {
        test("Testing caching sources") {
            val tmpDir = tempdir()
            withContext(Dispatchers.IO) {
                val srcDir = Files.createDirectory(Path.of(tmpDir.path, "src"))
                Files.createFile(Path.of(srcDir.pathString, "testSource.java"))
            }
            knowledgeBaseManager.save("testProject", tmpDir)
            knowledgeBaseManager.isCached("testProject") shouldBe true
            with(knowledgeBaseManager.load("testProject")) {
                shouldNotBeEmpty()
                length() != 0L
            }
            knowledgeBaseManager.clean("testProject")
            knowledgeBaseManager.isCached("testProject") shouldBe false
        }
    }
}
