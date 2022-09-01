package org.danilopianini.plagiarismdetector.utils

/**
 * A supplier of token, used for authenticating to API of repository services.
 */
interface AuthenticationTokenSupplierStrategy {
    /**
     * @return the token.
     */
    val token: String
}
