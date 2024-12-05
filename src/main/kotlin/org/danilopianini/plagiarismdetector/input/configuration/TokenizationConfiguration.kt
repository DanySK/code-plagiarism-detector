package org.danilopianini.plagiarismdetector.input.configuration

import org.danilopianini.plagiarismdetector.utils.Language

/**
 * An interface modeling a configuration related to the Tokenization technique.
 */
interface TokenizationConfiguration {
    /**
     * The language of sources to analyze.
     */
    val language: Language

    /**
     * The minimum number of tokens to search.
     */
    val minimumTokens: Int

    /**
     * The filter threshold to use in order to filter sources before comparison.
     */
    val filterThreshold: Double?
}

/**
 * A simple [TokenizationConfiguration] implementation.
 */
class TokenizationConfigurationImpl(
    override val language: Language,
    override val minimumTokens: Int,
    override val filterThreshold: Double?,
) : TokenizationConfiguration
