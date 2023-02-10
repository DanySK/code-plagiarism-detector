package org.danilopianini.plagiarismdetector.core.analyzer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.LanguageTokenTypesImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenTypeImpl

class LanguageTokenTypesTest : FunSpec() {
    private val tokenTypeName = "loop-stmt"
    private val constructs = setOf("ForEachStmt", "ForStmt", "WhileStmt", "DoStmt")
    private val languageTypes = LanguageTokenTypesImpl(
        setOf(
            TokenTypeImpl(tokenTypeName, constructs)
        )
    )

    init {
        test("Extracting the token type name by a construct") {
            with("ForEachStmt") {
                languageTypes.tokenFor(this).let {
                    it shouldNotBe null
                    it?.name shouldBe tokenTypeName
                    it?.languageConstructs shouldBe constructs
                }
            }
        }

        test("Trying to extract the token type name of a non-existing construct should return null") {
            languageTypes.tokenFor("non-existing-token-name") shouldBe null
        }
    }
}
