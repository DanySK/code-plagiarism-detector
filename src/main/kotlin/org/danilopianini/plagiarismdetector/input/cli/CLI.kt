package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.groups.defaultByName
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.restrictTo
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.input.cli.output.ExporterConfig
import org.danilopianini.plagiarismdetector.input.cli.output.PlainFileExporterConfig
import org.danilopianini.plagiarismdetector.input.cli.technique.TechniqueConfig
import org.danilopianini.plagiarismdetector.input.cli.technique.TokenizationConfig
import org.danilopianini.plagiarismdetector.output.Output

/**
 * The main Command Line Interface command.
 */
class CLI(output: Output) : CliktCommand() {
    override val printHelpOnEmptyArgs: Boolean = true

    override val allowMultipleSubcommands = true

    /**
     * The technique configuration.
     */
    val techniqueType by option(help = TECHNIQUE_HELP_MSG)
        .groupChoice<TechniqueConfig<Match>>(
            TOKENIZATION_COMMAND to TokenizationConfig(),
        ).defaultByName(TOKENIZATION_COMMAND)

    /**
     * The exporter configuration.
     */
    val exporterType by option(help = OUTPUT_HELP_MSG)
        .groupChoice<ExporterConfig<Match>>(
            PLAIN_TEXT_COMMAND to PlainFileExporterConfig(output),
        ).defaultByName(PLAIN_TEXT_COMMAND)

    /**
     * The percentage of duplicated code in a source file under which matches are not reported.
     */
    val minimumDuplication by option("--min-duplication", help = MIN_DUPLICATION_HELP_MSG)
        .double()
        .restrictTo(0.0..1.0)
        .default(DEFAULT_MIN_DUPLICATION_PERCENTAGE)

    /**
     * Files to exclude during the detection.
     */
    val exclude by option(help = EXCLUDE_HELP_MSG)
        .split(",")

    override fun help(context: Context) = HELP_MSG

    override fun run() = Unit

    private companion object {
        private const val HELP_MSG =
            "code-plagiarism-detector is a command line tool for scanning " +
                "existing projects in search of potential signs of plagiarism."
        private const val TOKENIZATION_COMMAND = "tokenization"
        private const val DEFAULT_TECHNIQUE = TOKENIZATION_COMMAND
        private const val PLAIN_TEXT_COMMAND = "plain-text"
        private const val DEFAULT_EXPORTER = PLAIN_TEXT_COMMAND
        private const val DEFAULT_MIN_DUPLICATION_PERCENTAGE = 0.3
        private const val MIN_DUPLICATION_HELP_MSG =
            "The percentage of duplicated code in " +
                "a source file under which matches are not reported. Default: $DEFAULT_MIN_DUPLICATION_PERCENTAGE."
        private const val EXCLUDE_HELP_MSG = "Comma separated list of files to be excluded from the check."
        private const val TECHNIQUE_HELP_MSG =
            "The technique used to analyze and detect similarities. " +
                "Default: $DEFAULT_TECHNIQUE."
        private const val OUTPUT_HELP_MSG = "Output type format. Default: $DEFAULT_EXPORTER."
    }
}
