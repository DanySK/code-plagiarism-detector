package org.danilopianini.plagiarismdetector.output.exporter

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.Output
import java.io.FileOutputStream
import java.io.PrintWriter
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

/**
 * An abstract class implementing a file exporter.
 * @property outputDirectory the [Path] of the directory where store the results.
 */
abstract class FileExporter<in M : Match>(
    private val outputDirectory: Path,
    private val output: Output,
) : ReportsExporter<M> {

    /**
     * Exports the given [reports] in a file inside [outputDirectory].
     */
    override operator fun invoke(reports: Set<Report<M>>) = reports
        .groupBy { it.submittedProject }
        .forEach {
            val fileName = "report-${it.key.name}.$fileExtension"
            val outputFile = Paths.get(outputDirectory.pathString, fileName).toFile()
            output.logInfo("Exporting report at ${outputFile.path}")
            PrintWriter(FileOutputStream(outputFile)).use { out ->
                export(reports, out)
            }
        }

    /**
     * The file extension of the file.
     */
    protected abstract val fileExtension: String

    /**
     * Formats the [reports] exporting them using the given [output].
     */
    protected abstract fun export(reports: Set<Report<M>>, output: PrintWriter)
}
