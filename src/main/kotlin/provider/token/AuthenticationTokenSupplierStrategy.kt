package provider.token

/**
 * A supplier of token.
 */
fun interface AuthenticationTokenSupplierStrategy {
    /**
     * @return the token.
     * @throws NullPointerException if the credentials cannot be retrieved.
     */
    fun getCredentials(): String
}

/**
 * A token supplier strategy which retrieves the token via environment variables.
 * @property environmentVariableName the name of the environment variable in which is stored the token.
 */
class EnvironmentTokenSupplier(
    private val environmentVariableName: String,
    private val separator: CharSequence = " ",
    private vararg val otherEnvironmentVariableNames: String
) : AuthenticationTokenSupplierStrategy {
    override fun getCredentials(): String {
        val tokenValues = listOf(System.getenv(environmentVariableName)) +
            otherEnvironmentVariableNames.map { System.getenv(it) }.toMutableList()
        if (tokenValues.any { it == null }) {
            throw NullPointerException(
                "Error while retrieving environment variables: variables not defined in the system environment."
            )
        }
        return tokenValues.reduce { acc, s -> acc.plus(separator).plus(s) }
    }
}
