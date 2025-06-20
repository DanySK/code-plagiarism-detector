package org.danilopianini.plagiarismdetector.core.filter.indexer.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenType
import org.danilopianini.plagiarismdetector.core.filter.indexer.Indexer

/**
 * A concrete [Indexer] for tokens. It generates key-value pairs: key refers
 * to [TokenType] and value refers to its occurrence frequency.
 *
 * For example: if inputted token sequence is
 * `[literal-expr, block-stmt, name-expr, if-stmt, name-expr, literal-expr, literal-expr]`,
 * resulted token index will contain 4 key-value pairs which are:
 * `[(literal-expr, 3), (block-stmt, 1), (name-expr, 2), (if-stmt, 1)]`
 */
class TokenBasedIndexer : Indexer<TokenizedSource, Sequence<Token>, Map<TokenType, Int>> {
    override operator fun invoke(input: TokenizedSource): Map<TokenType, Int> = input.representation
        .groupingBy { it.type }
        .eachCount()
}
