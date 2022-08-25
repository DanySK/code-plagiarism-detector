package provider

import com.mashape.unirest.http.Unirest
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import org.slf4j.LoggerFactory
import provider.criteria.BitbucketSearchCriteria
import provider.token.EnvironmentTokenSupplier
import repository.BitbucketRepository
import repository.Repository
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * A class implementing a search query for Bitbucket repositories.
 */
class BitbucketProvider : AbstractRepositoryProvider<String, BitbucketSearchCriteria>() {
    companion object {
        private const val AUTH_USERNAME = "BB_USER"
        private const val AUTH_TOKEN_NAME = "BB_TOKEN"
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
    }
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val tokenSupplier = EnvironmentTokenSupplier(AUTH_USERNAME, ":", AUTH_TOKEN_NAME)
    private val encodedAuthenticationToken = Base64.getEncoder()
        .encodeToString(tokenSupplier.getCredentials().toByteArray(StandardCharsets.UTF_8))

    override fun urlIsValid(url: URL): Boolean = url.host == BITBUCKET_HOST

    override fun getRepoByUrl(url: URL): Repository {
        return BitbucketRepository(
            getResponses(BASE_URL.plus(url.path)).first()
        )
    }

    override fun byCriteria(criteria: BitbucketSearchCriteria): Iterable<Repository> {
        return getResponses(BASE_URL.plus(criteria.apply())).asSequence()
            .flatMap { it.getJSONArray(VALUES_FIELD) }
            .map { BitbucketRepository(JSONObject(it.toString())) }
            .toSet()
    }

    private fun getResponses(url: String): Iterable<JSONObject> {
        val responses = mutableSetOf<JSONObject>()
        responses.add(doGETRequest(url))
        if (checkError(responses.last())) {
            throw IllegalArgumentException(responses.last().getJSONObject(ERROR_FIELD).get(MESSAGE_FIELD).toString())
        }
        if (hasNext(responses.last())) {
            responses.addAll(getResponses(responses.last().get(NEXT_PAGE_FIELD).toString()))
        }
        return responses
    }

    private fun doGETRequest(url: String): JSONObject {
        val response = Unirest.get(url)
            .header(AUTH_HEADER_FIELD, "$AUTH_HEADER_PREFIX $encodedAuthenticationToken")
            .header(ACCEPT_HEADER_FIELD, ACCEPT_HEADER_VALUE)
            .asBinary()
        logger.info("#fetch(GET $url): [${response.status} ${response.statusText}]")
        if (response.status == UNAUTHORIZED_CODE) {
            error("HTTP Response: ${response.statusText}")
        }
        return JSONObject(IOUtils.toString(response.body, StandardCharsets.UTF_8))
    }
    private fun checkError(responseObject: JSONObject) = !responseObject.isNull(ERROR_FIELD)

    private fun hasNext(responseObject: JSONObject) = !responseObject.isNull(NEXT_PAGE_FIELD)
}
