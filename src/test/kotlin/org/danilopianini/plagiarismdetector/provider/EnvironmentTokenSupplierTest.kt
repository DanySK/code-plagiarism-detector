package org.danilopianini.plagiarismdetector.provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier

class EnvironmentTokenSupplierTest : FunSpec() {
    companion object {
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
        private const val NON_EXISTING_TOKEN_VAR = "WRONG_TOKEN"
    }

    init {
        test("Trying to retrieve the content of a non-existing environment variable should throw an exception") {
            val supplier = EnvironmentTokenSupplier(NON_EXISTING_TOKEN_VAR)
            shouldThrow<IllegalArgumentException> {
                supplier.token
            }
        }

        test("The content of an existing environment variable should meet the formatting requirements") {
            val fakeEnv = mapOf(GH_AUTH_TOKEN_VAR to "GH_TOKEN")
            val supplier = EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR, getenv = fakeEnv::get)
            supplier.token.shouldNotBeEmpty()
            supplier.token.shouldContain("GH_TOKEN")
        }
    }
}
