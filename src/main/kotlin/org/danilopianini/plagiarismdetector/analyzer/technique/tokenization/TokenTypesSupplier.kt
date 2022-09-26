package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypes

/**
 * A supplier of [LanguageTokenTypes], responsible for
 * retrieving the lexical token types of a specific programming
 * language and encapsulating them in a [LanguageTokenTypes].
 */
interface TokenTypesSupplier {
    /**
     * The [LanguageTokenTypes] object.
     */
    val types: LanguageTokenTypes
}
