package org.danilopianini.plagiarismdetector.output.exporter

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Exports the given [reports] in a file inside [outputDirectory].
     */
    override operator fun invoke(reports: Set<Report<M>>) = reports
        .groupBy { it.submittedProject }
        .forEach {
            val fileName = "report-${it.key.name}.$fileExtension"
            val output = Paths.get(outputDirectory.pathString, fileName).toFile()
            logger.info("Exporting report at ${output.path}")
            PrintWriter(FileOutputStream(output)).use { out ->
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
