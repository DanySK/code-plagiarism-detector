package provider

import java.net.URL

/**
 * A provider of Bitbucket repositories.
 */
class BitbucketProvider : ProjectsProvider {
    private val _repositories: Iterable<Repository>

    constructor(url: URL) {
        _repositories = BitbucketSearchQuery().byLink(url)
    }

    constructor(searchCriteria: BitbucketSearchCriteria) {
        _repositories = BitbucketSearchQuery().byCriteria(searchCriteria)
    }

    override val repositories: Iterable<Repository>
        get() = _repositories
}
