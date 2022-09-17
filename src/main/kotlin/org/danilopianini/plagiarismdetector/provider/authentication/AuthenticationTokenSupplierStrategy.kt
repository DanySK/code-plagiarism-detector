package org.danilopianini.plagiarismdetector.provider.authentication

/**
 * A supplier of token, used for authenticating to API of repository services.
 */
interface AuthenticationTokenSupplierStrategy {
    /**
     * @return the token.
     */
    val token: String
}
