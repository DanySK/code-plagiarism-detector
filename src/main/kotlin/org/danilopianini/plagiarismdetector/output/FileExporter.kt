package org.danilopianini.plagiarismdetector.output

import org.danilopianini.plagiarismdetector.core.Result
import org.danilopianini.plagiarismdetector.core.detector.Match
import java.io.FileOutputStream
import java.io.PrintWriter
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import kotlin.io.path.pathString

/**
 * An abstract class implementing a file exporter.
 * @property outputDirectory the [Path] of the directory where store the results.
 */
abstract class FileExporter<in M : Match>(private val outputDirectory: Path) : ResultsExporter<M> {

    /**
     * Exports the given [results] in a file inside [outputDirectory].
     */
    override operator fun invoke(results: Set<Result<M>>) {
        val output = Paths.get(outputDirectory.pathString, "${LocalDateTime.now()}.txt").toFile()
        PrintWriter(FileOutputStream(output, true)).use {
            export(results, it)
        }
    }

    /**
     * Formats the [results] exporting them using the given [output].
     */
    protected abstract fun export(results: Set<Result<M>>, output: PrintWriter)
}
