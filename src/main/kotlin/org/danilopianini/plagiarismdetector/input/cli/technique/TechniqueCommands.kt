package org.danilopianini.plagiarismdetector.input.cli.technique

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.commons.Language
import org.danilopianini.plagiarismdetector.core.TechniqueFacade
import org.danilopianini.plagiarismdetector.core.TokenizationFacade
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatch
import org.danilopianini.plagiarismdetector.input.SupportedOptions

/**
 * Technique configurations.
 */
sealed class TechniqueConfig<out M : Match>(name: String) : OptionGroup(name = name) {

    /**
     * The language of sources to analyze.
     */
    val language by option(help = LANGUAGE_HELP_MSG)
        .choice(*SupportedOptions.languages.map(Language::name).toTypedArray())
        .convert { langName ->
            SupportedOptions.languages.find { langName == it.name } ?: error("$langName is not a supported language")
        }
        .default(Java)

    /**
     * Returns the [TechniqueFacade] for the specific chosen technique.
     */
    abstract fun getFacade(): TechniqueFacade<M>

    companion object {
        private const val LANGUAGE_HELP_MSG = "Sources code language. Default: Java."
    }
}

/**
 * Tokenization configurations.
 */
class TokenizationConfig : TechniqueConfig<TokenMatch>(TOKENIZATION_NAME) {

    /**
     * The minimum token length which should be reported as a duplicate.
     */
    val minimumTokens by option(help = MIN_TOKENS_HELP_MSG)
        .int()
        .default(DEFAULT_MIN_TOKENS)

    /**
     * The cutoff threshold used to filter comparison pairs.
     */
    val filterThreshold by option(help = FILTER_THRESHOLD_HELP_MSG)
        .double()
        .restrictTo(0.0..1.0)

    override fun getFacade(): TokenizationFacade = TokenizationFacade(this)

    companion object {
        private const val TOKENIZATION_NAME = "Tokenization options"
        private const val DEFAULT_MIN_TOKENS = 15
        private const val MIN_TOKENS_HELP_MSG = "The minimum token length which should be reported " +
            "as a duplicate. Default is $DEFAULT_MIN_TOKENS."
        private const val FILTER_THRESHOLD_HELP_MSG = "The cutoff threshold used to filter comparison pairs."
    }
}
