package org.danilopianini.plagiarismdetector.input.cli.output

import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.PlainFileExporter
import org.danilopianini.plagiarismdetector.output.ReportsExporter

/**
 * [PlainFileExporter] configurations.
 */
class PlainFileExporterConfig<in M : Match> : ExporterConfig<M>(PLAIN_FILE_NAME) {

    override fun getExporter(): ReportsExporter<M> = PlainFileExporter(outputPath)

    companion object {
        private const val PLAIN_FILE_NAME = "Plain file options"
    }
}
