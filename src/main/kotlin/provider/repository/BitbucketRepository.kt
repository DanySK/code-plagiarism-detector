package provider.repository

import org.json.JSONObject
import java.net.URL

private const val NAME_FIELD = "name"
private const val LINKS_FIELD = "links"
private const val CLONE_FIELD = "clone"
private const val HREF_FIELD = "href"

/**
 * A Bitbucket repository adapter.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : AbstractRepository() {
    override val name: String
        get() = repositoryInfo.get(NAME_FIELD).toString()

    override val owner: String
        get() = repositoryInfo.getJSONObject("owner").get("display_name").toString()

    override val cloneUrl: URL
        get() {
            val cloneInfos = repositoryInfo.getJSONObject(LINKS_FIELD)
                .getJSONArray(CLONE_FIELD)
                .get(0) as JSONObject
            return URL(cloneInfos.get(HREF_FIELD).toString())
        }
}
