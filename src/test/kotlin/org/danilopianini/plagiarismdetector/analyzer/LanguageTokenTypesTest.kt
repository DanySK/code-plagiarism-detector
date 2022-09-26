package org.danilopianini.plagiarismdetector.analyzer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypesImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenTypeImpl

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
                languageTypes.isToken(this) shouldBe true
                languageTypes.tokenFor(this).name shouldBe tokenTypeName
                languageTypes.tokenFor(this).languageConstructs shouldBe constructs
            }
        }

        test("Trying to extract the token type name of a non-existing construct") {
            shouldThrow<IllegalStateException> {
                languageTypes.tokenFor("non-existing-token-name")
            }
        }
    }
}
