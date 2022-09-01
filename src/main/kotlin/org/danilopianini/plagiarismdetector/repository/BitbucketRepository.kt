package org.danilopianini.plagiarismdetector.repository

import org.json.JSONObject
import java.net.URL

/**
 * A Bitbucket repository.
 * @property repositoryInfo the [JSONObject] containing all repo's infos.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : AbstractRepository() {
    companion object {
        private const val REPOSITORY_NAME_FIELD = "name"
        private const val LINKS_FIELD = "links"
        private const val CLONE_FIELD = "clone"
        private const val HREF_FIELD = "href"
        private const val OWNER_FIELD = "owner"
        private const val OWNER_NAME_FIELD = "display_name"
    }

    override val name: String by lazy {
        repositoryInfo.get(REPOSITORY_NAME_FIELD).toString()
    }

    override val owner: String by lazy {
        repositoryInfo.getJSONObject(OWNER_FIELD).get(OWNER_NAME_FIELD).toString()
    }

    override val cloneUrl: URL by lazy {
        repositoryInfo.getJSONObject(LINKS_FIELD)
            .getJSONArray(CLONE_FIELD)
            .first()
            .let(::JSONObject)
            .let { URL(it.get(HREF_FIELD).toString()) }
    }

    override fun toString() = "git@bitbucket.org:$owner/$name.git"
}
