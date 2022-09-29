package org.danilopianini.plagiarismdetector.input

import com.github.ajalt.clikt.core.subcommands
import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSession
import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSessionImpl
import org.danilopianini.plagiarismdetector.input.cli.CLI
import org.danilopianini.plagiarismdetector.input.cli.provider.CorpusProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.SubmissionProviderCommand
import org.danilopianini.plagiarismdetector.input.configuration.RunConfigurationImpl

/**
 * A concrete [RunConfigurator] which parses CLI arguments to create a new run configuration.
 */
class CLIConfigurator : RunConfigurator {

    override fun sessionFrom(arguments: List<String>): AntiPlagiarismSession {
        val submissionCommand = SubmissionProviderCommand()
        val corpusCommand = CorpusProviderCommand()
        val commonCommand = CLI()
        commonCommand.subcommands(submissionCommand, corpusCommand).main(arguments)
        val config = RunConfigurationImpl(
            technique = commonCommand.technique.getFacade(),
            minDuplicatedPercentage = commonCommand.minimumDuplication,
            submission = submissionCommand.getRepositories().toSet(),
            corpus = corpusCommand.getRepositories().toSet(),
            filesToExclude = commonCommand.exclude?.toSet() ?: emptySet(),
            exporter = commonCommand.exporter.getExporter()
        )
        return AntiPlagiarismSessionImpl(config)
    }
}
