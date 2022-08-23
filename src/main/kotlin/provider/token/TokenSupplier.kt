package provider.token

/**
 * A supplier of token.
 */
fun interface TokenSupplier {
    /**
     * @return the token
     */
    fun getToken(): String
}
