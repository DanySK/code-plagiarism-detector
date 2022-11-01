package org.danilopianini.plagiarismdetector.input.cli.output

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.PlainFileExporter
import org.danilopianini.plagiarismdetector.output.ReportsExporter
import java.nio.file.Path

/**
 * Output configurations.
 */
sealed class ExporterConfig<in M : Match>(name: String) : OptionGroup(name = name) {

    /**
     * The path of the directory where to store the reports.
     */
    val outputPath: Path by option("--o", "--output-dir", help = OUTPUT_PATH_HELP_MSG)
        .file(mustExist = true, canBeFile = false, mustBeWritable = true)
        .convert { it.toPath() }
        .required()

    /**
     * Returns the [ReportsExporter] for the specific chosen exporter.
     */
    abstract fun getExporter(): ReportsExporter<M>

    companion object {
        private const val OUTPUT_PATH_HELP_MSG = "The path of the directory where to store the reports."
    }
}

/**
 * Plain file configurations.
 */
class PlainFileConfig<in M : Match> : ExporterConfig<M>(PLAIN_FILE_NAME) {

    override fun getExporter(): ReportsExporter<M> = PlainFileExporter(outputPath)

    companion object {
        private const val PLAIN_FILE_NAME = "Plain file options"
    }
}
