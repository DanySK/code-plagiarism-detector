package org.danilopianini.plagiarismdetector.analyzer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.GramImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenTypeImpl

class TokenizedSourceTest : FunSpec() {
    private val token1 = TokenImpl(0, 0, TokenTypeImpl("A", emptySet()))
    private val token2 = TokenImpl(0, 1, TokenTypeImpl("B", emptySet()))
    private val token3 = TokenImpl(0, 2, TokenTypeImpl("C", emptySet()))
    private val token4 = TokenImpl(0, 3, TokenTypeImpl("D", emptySet()))
    private val token5 = TokenImpl(0, 4, TokenTypeImpl("E", emptySet()))
    private val tokenizedSource = spyk(TokenizedSourceImpl(mockk(), mockk())) {
        every { representation } returns sequenceOf(token1, token2, token3, token4, token5)
    }

    init {
        test("Token grams are correctly generated") {
            val gramSize = 3
            val expectedGrams = listOf(
                GramImpl(listOf(token1, token2, token3)),
                GramImpl(listOf(token2, token3, token4)),
                GramImpl(listOf(token3, token4, token5))
            )
            val grams = tokenizedSource.splitInGramsOf(gramSize)
            grams.count() shouldBeExactly gramSize
            grams.toList() shouldBe expectedGrams
        }
    }
}
