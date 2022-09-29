package org.danilopianini.plagiarismdetector.core.filter.indexer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenTypeImpl
import org.danilopianini.plagiarismdetector.core.filter.indexer.technique.tokenization.TokenBasedIndexer

class TokenBasedIndexerTest : FunSpec() {

    private val indexer = TokenBasedIndexer()

    init {
        test("Test generation of right token indexes") {
            val sampleTokens = listOf(
                mockedToken("literal-expr"),
                mockedToken("block-stmt"),
                mockedToken("name-expr"),
                mockedToken("if-stmt"),
                mockedToken("name-expr"),
                mockedToken("literal-expr"),
                mockedToken("literal-expr"),
            )
            val tokenizedSource = spyk(TokenizedSourceImpl(mockk(), sampleTokens))
            val expectedResult = mapOf(
                "literal-expr" to 3,
                "block-stmt" to 1,
                "name-expr" to 2,
                "if-stmt" to 1,
            )
            indexer(tokenizedSource).mapKeys { it.key.name } shouldBe expectedResult
        }
    }

    private fun mockedToken(tokenTypeName: String) =
        mockk<TokenImpl> { every { type } returns TokenTypeImpl(tokenTypeName, emptySet()) }
}
