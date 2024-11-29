package org.danilopianini.plagiarismdetector.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenBasedComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatch

/**
 * A test class for testing project similarity estimations.
 *
 * Note: to know how percentile value is calculated see [`UnivariateStatistic` java doc](shorturl.at/egozH)
 */
class ProjectsSimilarityEstimatorTest : FunSpec({

    test("Testing similarity estimation between projects with no matches") {
        val report =
            ReportImpl(
                mockk(),
                mockk(),
                emptySet<ComparisonResult<TokenMatch>>(),
                0.0,
            )
        report.similarity shouldBeExactly 0.0
    }

    test("Testing similarity estimation between projects with a single matches") {
        val reportedRatio = 0.5
        val similarity =
            SimilarityEstimatorWithConstantWeight()(
                reportedRatio,
                setOf(
                    TokenBasedComparisonResult(0.63, emptySet()),
                    TokenBasedComparisonResult(1.0, emptySet()),
                    TokenBasedComparisonResult(0.44, emptySet()),
                    TokenBasedComparisonResult(0.78, emptySet()),
                ),
            )
        val weightCoefficient = reportedRatio * 1.5
        val percentileValue = 0.945
        similarity.shouldBe(weightCoefficient * percentileValue plusOrMinus 0.00001)
    }
})
