package org.danilopianini.plagiarismdetector.input.cli.output

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import java.nio.file.Path
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.exporter.ReportsExporter

/**
 * An abstract class encapsulating [ReportsExporter] configuration.
 */
sealed class ExporterConfig<in M : Match>(name: String) : OptionGroup(name = name) {
    /**
     * The path of the directory where to store the reports.
     */
    val outputPath: Path by option("--o", "--output-dir", help = OUTPUT_PATH_HELP_MSG)
        .file(mustExist = false, canBeFile = false)
        .convert {
            require(it.isDirectory || it.mkdirs())
            it.toPath()
        }.required()

    /**
     * The [ReportsExporter] for the specific chosen exporter type.
     */
    abstract val exporter: ReportsExporter<M>

    private companion object {
        private const val OUTPUT_PATH_HELP_MSG = "The path of the directory where to store the reports."
    }
}
