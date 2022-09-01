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
        get() = (
            sequenceOf(System.getenv(environmentVariableName)) +
                otherEnvironmentVariableNames.asSequence().map(System::getenv)
            ).requireNoNulls()
            .joinToString(separator)
}
