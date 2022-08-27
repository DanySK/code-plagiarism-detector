package org.danilopianini.plagiarismdetector.utils

/**
 * A token supplier strategy which retrieves the token via environment variables.
 * @property environmentVariableName the name of the environment variable.
 * @property otherEnvironmentVariableNames the names of other environment variables.
 * @property separator the separator with which to concatenate the variables content.
 * The default is the empty character ("").
 */
class EnvironmentTokenSupplier(
    private val environmentVariableName: String,
    private vararg val otherEnvironmentVariableNames: String,
    private val separator: CharSequence = ""
) : AuthenticationTokenSupplierStrategy {
    override val token: String
        get() {
            val tokenValues = listOf(System.getenv(environmentVariableName)) +
                otherEnvironmentVariableNames.map(System::getenv).toList()
            if (tokenValues.any { it == null }) {
                throw NullPointerException(
                    "Error while retrieving environment variables: variables not defined in the system environment."
                )
            }
            return tokenValues.reduce { acc, s -> acc.plus(separator).plus(s) }
        }
}
