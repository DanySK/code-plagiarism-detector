package org.danilopianini.plagiarismdetector.provider

import com.mashape.unirest.http.Unirest
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import org.danilopianini.plagiarismdetector.provider.criteria.BitbucketSearchCriteria
import org.danilopianini.plagiarismdetector.repository.BitbucketRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.AuthenticationTokenSupplierStrategy
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Base64

private const val BITBUCKET_HOST = "bitbucket.org"
private const val BASE_URL = "https://api.bitbucket.org/2.0/repositories"
private const val ERROR_FIELD = "error"
private const val NEXT_PAGE_FIELD = "next"
private const val VALUES_FIELD = "values"
private const val MESSAGE_FIELD = "message"
private const val ACCEPT_HEADER_FIELD = "Accept"
private const val ACCEPT_HEADER_VALUE = "application/json"
private const val AUTH_HEADER_FIELD = "Authorization"
private const val AUTH_HEADER_PREFIX = "Basic"
private const val UNAUTHORIZED_CODE = 401
private const val FORBIDDEN_CODE = 403

/**
 * A provider of Bitbucket repositories.
 */
class BitbucketProvider private constructor(
    private var encodedAuthenticationToken: String? = null
) : AbstractRepositoryProvider<String, String, BitbucketSearchCriteria>() {
    companion object {

        /**
         * Creates a [BitbucketProvider] with anonymous authentication: this is **not** recommended due to
         * rate limits. In case limits are reached an exception is thrown.
         * See Bitbucket API doc [here](https://support.atlassian.com/bitbucket-cloud/docs/api-request-limits).
         */
        fun connectAnonymously() = BitbucketProvider()

        /**
         * Creates a [BitbucketProvider] with an authenticated connection.
         * @param tokenSupplier the supplier of the token
         */
        fun connectWithToken(tokenSupplier: AuthenticationTokenSupplierStrategy) =
            BitbucketProvider(
                Base64.getEncoder().encodeToString(
                    tokenSupplier.token.toByteArray(StandardCharsets.UTF_8)
                )
            )
    }

    override fun urlIsValid(url: URL): Boolean = url.host == BITBUCKET_HOST

    override fun getRepoByUrl(url: URL): Repository {
        return BitbucketRepository(
            getResponses(BASE_URL.plus(url.path)).first()
        )
    }

    override fun byCriteria(criteria: BitbucketSearchCriteria): Iterable<Repository> {
        return getResponses(criteria.apply(BASE_URL)).asSequence()
            .flatMap { it.getJSONArray(VALUES_FIELD) }
            .map { BitbucketRepository(JSONObject("$it")) }
            .toSet()
    }

    private fun getResponses(url: String): Iterable<JSONObject> {
        val responses = mutableSetOf<JSONObject>()
        responses.add(doGETRequest(url))
        require(!checkError(responses.last())) {
            responses.last().getJSONObject(ERROR_FIELD).get(MESSAGE_FIELD).toString()
        }
        if (hasNext(responses.last())) {
            responses.addAll(getResponses(responses.last().get(NEXT_PAGE_FIELD).toString()))
        }
        return responses
    }

    private fun doGETRequest(url: String): JSONObject {
        val request = Unirest.get(url)
            .header(ACCEPT_HEADER_FIELD, ACCEPT_HEADER_VALUE)
        if (encodedAuthenticationToken != null) {
            request.header(AUTH_HEADER_FIELD, "$AUTH_HEADER_PREFIX $encodedAuthenticationToken")
        }
        val response = request.asBinary()
        check(response.status != UNAUTHORIZED_CODE && response.status != FORBIDDEN_CODE) {
            "HTTP Response: ${response.statusText}. ${IOUtils.toString(response.body, StandardCharsets.UTF_8)}"
        }
        return JSONObject(IOUtils.toString(response.body, StandardCharsets.UTF_8))
    }

    private fun checkError(responseObject: JSONObject) = !responseObject.isNull(ERROR_FIELD)

    private fun hasNext(responseObject: JSONObject) = !responseObject.isNull(NEXT_PAGE_FIELD)
}
