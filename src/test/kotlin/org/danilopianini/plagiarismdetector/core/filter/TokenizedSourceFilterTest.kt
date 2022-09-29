package org.danilopianini.plagiarismdetector.core.filter

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenTypeImpl
import org.danilopianini.plagiarismdetector.core.filter.technique.tokenization.TokenizedSourceFilter

class TokenizedSourceFilterTest : FunSpec({

    test("test tokenized source filter") {
        val submission = TokenizedSourceImpl(
            mockk(),
            listOf(
                TokenImpl(0, 0, TokenTypeImpl("literal-expr", emptySet())),
                TokenImpl(0, 1, TokenTypeImpl("binary-expr", emptySet())),
                TokenImpl(0, 2, TokenTypeImpl("name-expr", emptySet())),
                TokenImpl(0, 3, TokenTypeImpl("literal-expr", emptySet())),
                TokenImpl(0, 4, TokenTypeImpl("unary-expr", emptySet())),
                TokenImpl(0, 5, TokenTypeImpl("name-expr", emptySet())),
                TokenImpl(0, 6, TokenTypeImpl("block-stmt", emptySet())),
                TokenImpl(0, 7, TokenTypeImpl("literal-expr", emptySet()))
            )
        )
        val expectedResult = TokenizedSourceImpl(
            mockk(),
            listOf(
                TokenImpl(2, 0, TokenTypeImpl("literal-expr", emptySet())),
                TokenImpl(2, 1, TokenTypeImpl("unary-expr", emptySet())),
                TokenImpl(2, 2, TokenTypeImpl("name-expr", emptySet())),
                TokenImpl(2, 3, TokenTypeImpl("block-stmt", emptySet())),
                TokenImpl(2, 4, TokenTypeImpl("literal-expr", emptySet()))
            )
        )
        val corpus = sequenceOf(
            TokenizedSourceImpl(
                mockk(),
                listOf(
                    TokenImpl(1, 0, TokenTypeImpl("loop-stmt", emptySet())),
                    TokenImpl(1, 1, TokenTypeImpl("variable-decl-expr", emptySet())),
                    TokenImpl(1, 2, TokenTypeImpl("variable-declarator", emptySet())),
                    TokenImpl(1, 3, TokenTypeImpl("literal-expr", emptySet())),
                    TokenImpl(1, 4, TokenTypeImpl("unary-expr", emptySet())),
                    TokenImpl(1, 5, TokenTypeImpl("literal-expr", emptySet())),
                    TokenImpl(1, 6, TokenTypeImpl("binary-expr", emptySet()))
                )
            ),
            expectedResult
        )
        TokenizedSourceFilter()(submission, corpus).toList() shouldBe listOf(expectedResult)
    }
})
