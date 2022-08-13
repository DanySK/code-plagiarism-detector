package provider

import org.json.JSONObject
import java.io.InputStream

private const val NAME_FIELD = "name"

/**
 * A Bitbucket repository adapter.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : Repository {
    override val name: String
        get() = repositoryInfo.get(NAME_FIELD).toString()

    override val contributors: Iterable<String>
        get() = TODO("Not yet implemented")

    override val sources: Iterable<InputStream>
        get() = TODO("Not yet implemented")
}
