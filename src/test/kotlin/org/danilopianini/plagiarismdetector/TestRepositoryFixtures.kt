package org.danilopianini.plagiarismdetector

import java.io.File
import java.nio.file.Files
import org.eclipse.jgit.api.Git

fun gitRepositoryWith(vararg files: Pair<String, String>): File {
    val directory = Files.createTempDirectory("repository").toFile()
    Git.init().setDirectory(directory).setInitialBranch("main").call().use { git ->
        files.forEach { (path, content) ->
            File(directory, path).apply {
                parentFile.mkdirs()
                writeText(content)
            }
        }
        git.add().addFilepattern(".").call()
        git.commit()
            .setAuthor("test", "test@example.org")
            .setCommitter("test", "test@example.org")
            .setMessage("Initial commit")
            .setSign(false)
            .call()
    }
    return directory
}
