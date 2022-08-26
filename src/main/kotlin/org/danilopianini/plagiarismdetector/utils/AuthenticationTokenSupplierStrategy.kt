package org.danilopianini.plagiarismdetector.utils

/**
 * A supplier of token, used for authenticating to repositories API services.
 */
interface AuthenticationTokenSupplierStrategy {
    /**
     * @return the token.
     * @throws NullPointerException if the token cannot be retrieved.
     */
    val token: String
}
