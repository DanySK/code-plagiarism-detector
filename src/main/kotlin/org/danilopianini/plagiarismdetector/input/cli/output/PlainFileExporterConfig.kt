package org.danilopianini.plagiarismdetector.input.cli.output

import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.PlainFileExporter
import org.danilopianini.plagiarismdetector.output.ReportsExporter

/**
 * A concrete [PlainFileExporter] configuration.
 */
class PlainFileExporterConfig<in M : Match> : ExporterConfig<M>(PLAIN_FILE_NAME) {

    override val exporter: ReportsExporter<M> by lazy {
        PlainFileExporter(outputPath)
    }

    companion object {
        private const val PLAIN_FILE_NAME = "Plain file options"
    }
}
