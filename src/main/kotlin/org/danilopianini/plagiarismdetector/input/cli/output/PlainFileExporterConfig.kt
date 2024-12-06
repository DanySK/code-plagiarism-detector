package org.danilopianini.plagiarismdetector.input.cli.output

import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.Output
import org.danilopianini.plagiarismdetector.output.exporter.PlainFileExporter
import org.danilopianini.plagiarismdetector.output.exporter.ReportsExporter

/**
 * A concrete [PlainFileExporter] configuration.
 */
class PlainFileExporterConfig<in M : Match>(
    output: Output,
) : ExporterConfig<M>(PLAIN_FILE_NAME) {
    override val exporter: ReportsExporter<M> by lazy {
        PlainFileExporter(outputPath, output)
    }

    private companion object {
        private const val PLAIN_FILE_NAME = "Plain file options"
    }
}
