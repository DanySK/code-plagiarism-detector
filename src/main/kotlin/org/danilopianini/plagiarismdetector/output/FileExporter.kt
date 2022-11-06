package org.danilopianini.plagiarismdetector.output

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.detector.Match
import java.io.FileOutputStream
import java.io.PrintWriter
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

/**
 * An abstract class implementing a file exporter.
 * @property outputDirectory the [Path] of the directory where store the results.
 */
abstract class FileExporter<in M : Match>(private val outputDirectory: Path) : ReportsExporter<M> {

    /**
     * Exports the given [reports] in a file inside [outputDirectory].
     */
    override operator fun invoke(reports: Set<Report<M>>) {
        val submittedProjectNames = reports.map { it.submittedProject.name }.distinct().joinToString("-")
        val fileName = "report-$submittedProjectNames.txt"
        val output = Paths.get(outputDirectory.pathString, fileName).toFile()
        PrintWriter(FileOutputStream(output)).use {
            export(reports, it)
        }
    }

    /**
     * Formats the [reports] exporting them using the given [output].
     */
    protected abstract fun export(reports: Set<Report<M>>, output: PrintWriter)
}
