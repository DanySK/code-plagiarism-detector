package org.danilopianini.plagiarismdetector.analyzer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypes
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.FileTokenTypesSupplier

class FileTokenTypesSupplierTest : FunSpec() {
    companion object {
        private const val CONFIG_FILE_NAME = "java-token-types.yml"
    }

    init {
        test("Trying to load java token types from a non-valid config file") {
            shouldThrow<IllegalStateException> {
                FileTokenTypesSupplier("non-existing-file.yml")
            }
        }

        test("Loading java token types from an existing config file") {
            FileTokenTypesSupplier(CONFIG_FILE_NAME).types.shouldBeInstanceOf<LanguageTokenTypes>()
        }
    }
}
