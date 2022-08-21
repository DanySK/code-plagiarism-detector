package provider

import java.net.URL

/**
 * A provider of Bitbucket repositories.
 */
class BitbucketProvider : ProjectsProvider {
    private val _repositories: Iterable<Repository>

    constructor(url: URL) {
        val res = BitbucketSearchQuery().byLink(url)
        _repositories = if (res != null) setOf(res) else emptySet()
    }

    constructor(searchCriteria: BitbucketSearchCriteria) {
        _repositories = BitbucketSearchQuery().byCriteria(searchCriteria)
    }

    override val repositories: Iterable<Repository>
        get() = _repositories
}
