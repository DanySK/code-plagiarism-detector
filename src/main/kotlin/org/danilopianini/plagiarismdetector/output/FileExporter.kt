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
    override operator fun invoke(reports: Set<Report<M>>) = reports
        .groupBy { it.submittedProject }
        .forEach {
            val fileName = "report-${it.key.name}.$fileExtension"
            val summarizedReportFileName = "summarized-$fileName"
            val output = Paths.get(outputDirectory.pathString, fileName).toFile()
            PrintWriter(FileOutputStream(output)).use { out ->
                export(reports, out)
            }
            val summarizedReportFile = Paths.get(outputDirectory.pathString, summarizedReportFileName).toFile()
            PrintWriter(FileOutputStream(summarizedReportFile)).use {
                exportSummary(reports, it)
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

    /**
     * Creates the file with the summarized results from the given set of [Report] using the [output].
     */
    protected abstract fun exportSummary(reports: Set<Report<M>>, output: PrintWriter)
}
