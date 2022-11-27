package org.danilopianini.plagiarismdetector.input.cli.technique

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import org.danilopianini.plagiarismdetector.utils.Java
import org.danilopianini.plagiarismdetector.utils.Language
import org.danilopianini.plagiarismdetector.core.TechniqueFacade
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.input.SupportedOptions

/**
 * An abstract class encapsulating specific technique configuration.
 */
sealed class TechniqueConfig<out M : Match>(name: String) : OptionGroup(name = name) {

    /**
     * The language of sources to analyze.
     */
    protected val language by option(help = LANGUAGE_HELP_MSG)
        .choice(*SupportedOptions.languages.map(Language::name).toTypedArray())
        .convert { langName ->
            SupportedOptions.languages.find { langName == it.name } ?: error("$langName is not a supported language")
        }
        .default(Java)

    /**
     * Returns the [TechniqueFacade] for the specific chosen technique.
     */
    abstract val facade: TechniqueFacade<M>

    companion object {
        private const val LANGUAGE_HELP_MSG = "Sources code language. Default: Java."
    }
}
