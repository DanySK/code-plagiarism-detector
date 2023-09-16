package org.danilopianini.plagiarismdetector.core.detector

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSourceImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenTypeImpl
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.AverageSimilarityEstimator
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.MaxSimilarityEstimator
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.PenalizedAverageSimilarityEstimator
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.PenalizedMaxSimilarityEstimator
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatchImpl

class TokenBasedSimilarityStrategiesTest : FunSpec() {

    private val representation = Pair(
        TokenizedSourceImpl(
            mockk(),
            listOf(
                TokenImpl(0, 0, TokenTypeImpl("A", emptySet())),
                TokenImpl(0, 1, TokenTypeImpl("B", emptySet())),
                TokenImpl(0, 2, TokenTypeImpl("C", emptySet())),
                TokenImpl(0, 3, TokenTypeImpl("E", emptySet())),
            ),
        ),
        TokenizedSourceImpl(
            mockk(),
            listOf(
                TokenImpl(1, 0, TokenTypeImpl("A", emptySet())),
                TokenImpl(1, 1, TokenTypeImpl("B", emptySet())),
                TokenImpl(1, 2, TokenTypeImpl("H", emptySet())),
                TokenImpl(1, 3, TokenTypeImpl("C", emptySet())),
                TokenImpl(1, 4, TokenTypeImpl("E", emptySet())),
                TokenImpl(1, 5, TokenTypeImpl("D", emptySet())),
            ),
        ),
    )
    private val matches = setOf(
        TokenMatchImpl(mockk(), mockk(), 2),
        TokenMatchImpl(mockk(), mockk(), 2),
    )

    init {
        test("Testing `NormalizedAverageSimilarityStrategy` with no matches") {
            PenalizedAverageSimilarityEstimator().similarityOf(representation, emptySet()) shouldBe 0
        }

        test("Testing `NormalizedMaxSimilarityStrategy` with no matches") {
            PenalizedMaxSimilarityEstimator().similarityOf(representation, emptySet()) shouldBe 0
        }

        test("Testing `MaxSimilarityStrategy` with no matches") {
            MaxSimilarityEstimator().similarityOf(representation, emptySet()) shouldBe 0
        }

        test("Testing `AverageSimilarityStrategy` with no matches") {
            AverageSimilarityEstimator().similarityOf(representation, emptySet()) shouldBe 0
        }

        test("Testing `NormalizedAverageSimilarityStrategy` with sample data") {
            PenalizedAverageSimilarityEstimator().similarityOf(representation, matches) shouldBe 0.6
        }

        test("Testing `NormalizedMaxSimilarityStrategy` with sample data") {
            PenalizedMaxSimilarityEstimator().similarityOf(representation, matches) shouldBe 0.75
        }

        test("Testing `AverageSimilarityStrategy` with sample data") {
            AverageSimilarityEstimator().similarityOf(representation, matches) shouldBe 0.8
        }

        test("Testing `MaxSimilarityStrategy` with sample data") {
            MaxSimilarityEstimator().similarityOf(representation, matches) shouldBe 1.0
        }
    }
}
