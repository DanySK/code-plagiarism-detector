package org.danilopianini.plagiarismdetector

import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSessionImpl
import org.danilopianini.plagiarismdetector.input.cli.CLIConfigurator
import org.danilopianini.plagiarismdetector.output.StandardOutput
import org.danilopianini.plagiarismdetector.utils.LoggerConfiguratorImpl

/**
 * Code plagiarism detector entry point.
 */
fun main(args: Array<String>) {
    val configurationArgs = LoggerConfiguratorImpl()(args.toList())
    val output = StandardOutput()
    val configuration = CLIConfigurator(output)(configurationArgs)
    AntiPlagiarismSessionImpl(configuration, output)()
}
