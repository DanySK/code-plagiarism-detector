package org.danilopianini.plagiarismdetector.input.cli.output

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.ReportsExporter
import java.nio.file.Path

/**
 * A class encapsulating [ReportsExporter] configurations.
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
     * The [ReportsExporter] for the specific chosen exporter type.
     */
    abstract val exporter: ReportsExporter<M>

    companion object {
        private const val OUTPUT_PATH_HELP_MSG = "The path of the directory where to store the reports."
    }
}
