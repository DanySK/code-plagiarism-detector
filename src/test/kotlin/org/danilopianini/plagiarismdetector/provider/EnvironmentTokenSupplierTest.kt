package org.danilopianini.plagiarismdetector.provider

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.system.OverrideMode
import io.kotest.extensions.system.withEnvironment
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier

class EnvironmentTokenSupplierTest : FunSpec() {
    companion object {
        private const val BB_AUTH_USER_VAR = "BB_USER"
        private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
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
            withEnvironment(
                mapOf(BB_AUTH_USER_VAR to "BB_USER", BB_AUTH_TOKEN_VAR to "BB_TOKEN"),
                mode = OverrideMode.SetOrOverride,
            ) {
                val supplier = EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":")
                supplier.token.shouldNotBeEmpty()
                supplier.token.shouldContain(Regex(".+:.+"))
            }
        }
    }
}
