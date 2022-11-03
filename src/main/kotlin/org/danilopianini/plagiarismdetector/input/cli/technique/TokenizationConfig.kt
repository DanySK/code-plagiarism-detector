package org.danilopianini.plagiarismdetector.input.cli.technique

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import org.danilopianini.plagiarismdetector.core.TokenizationFacade
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatch

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
