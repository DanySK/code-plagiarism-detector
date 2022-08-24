package provider.token

/**
 * A supplier of token.
 */
interface TokenSupplierStrategy {
    /**
     * @return the token.
     */
    val token: String
}

/**
 * A token supplier strategy which retrieves the token via environment variables.
 * @property environmentVariableName the name of the environment variable in which is stored the token.
 */
class EnvironmentTokenSupplier(private val environmentVariableName: String) : TokenSupplierStrategy {
    override val token: String
        get() = System.getenv(environmentVariableName)
}
