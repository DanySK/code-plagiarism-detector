package provider

import com.mashape.unirest.http.Unirest
import org.json.JSONObject
import org.slf4j.LoggerFactory
import provider.criteria.BitbucketSearchCriteria
import provider.token.EnvironmentTokenSupplier
import provider.token.TokenSupplierStrategy
import repository.BitbucketRepository
import repository.Repository
import java.net.URL

/**
 * A class implementing a search query for Bitbucket repositories.
 */
class BitbucketProvider : AbstractRepositoryProvider<String, BitbucketSearchCriteria>() {
    companion object {
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
        private const val AUTH_HEADER_PREFIX = "Bearer+"
    }
    private val tokenSupplierStrategy: TokenSupplierStrategy = EnvironmentTokenSupplier(AUTH_TOKEN_NAME)
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun urlIsValid(url: URL): Boolean = url.host == BITBUCKET_HOST

    override fun getRepoByUrl(url: URL): Repository? {
        val response = getResponse(BASE_URL.plus(url.path))
        if (isError(response)) {
            logger.error(response.getJSONObject(ERROR_FIELD).get(MESSAGE_FIELD).toString())
            return null
        }
        return BitbucketRepository(response)
    }

    override fun byCriteria(criteria: BitbucketSearchCriteria): Iterable<Repository> {
        val result = mutableSetOf<Repository>()
        var response = getResponse(BASE_URL.plus(criteria.apply()))
        result.addAll(extractRepositories(response))
        while (hasNext(response)) {
            response = getResponse(response.get(NEXT_PAGE_FIELD).toString())
            result.addAll(extractRepositories(response))
        }
        return result
    }

    private fun getResponse(url: String): JSONObject {
        return Unirest.get(url)
            .header(ACCEPT_HEADER_FIELD, ACCEPT_HEADER_VALUE)
            .header(AUTH_HEADER_FIELD, "$AUTH_HEADER_PREFIX${tokenSupplierStrategy.token}")
            .asJson().body.`object`
    }

    private fun extractRepositories(responseObject: JSONObject): Iterable<Repository> {
        return if (isError(responseObject)) {
            logger.error(responseObject.getJSONObject(ERROR_FIELD).get(MESSAGE_FIELD).toString())
            emptySet()
        } else {
            responseObject.getJSONArray(VALUES_FIELD)
                .asSequence()
                .map { BitbucketRepository(JSONObject(it.toString())) }
                .toSet()
        }
    }

    private fun isError(responseObject: JSONObject) = !responseObject.isNull(ERROR_FIELD)

    private fun hasNext(responseObject: JSONObject) = !responseObject.isNull(NEXT_PAGE_FIELD)
}
