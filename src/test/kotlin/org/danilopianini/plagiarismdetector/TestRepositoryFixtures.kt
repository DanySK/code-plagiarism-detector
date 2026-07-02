package org.danilopianini.plagiarismdetector

import java.io.File
import java.nio.file.Files
import org.eclipse.jgit.api.Git

fun gitRepositoryWith(vararg files: Pair<String, String>): File {
    val directory = Files.createTempDirectory("repository").toFile()
    Git.init().setDirectory(directory).call().use { git ->
        files.forEach { (path, content) ->
            File(directory, path).apply {
                parentFile.mkdirs()
                writeText(content)
            }
        }
        git.add().addFilepattern(".").call()
        val status = git.status().call()
        check(status.added.isNotEmpty()) {
            "No files staged in test repository at $directory. Status: $status"
        }
        val commit = git.commit()
            .setAuthor("test", "test@example.org")
            .setCommitter("test", "test@example.org")
            .setMessage("Initial commit")
            .setSign(false)
            .setAllowEmpty(false)
            .call()
        checkNotNull(commit.tree) {
            "Created commit has no tree in test repository at $directory"
        }
    }
    return directory
}
