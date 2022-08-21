package provider

import provider.query.GitHubSearchCriteria
import provider.query.GitHubSearchQuery
import provider.repository.Repository
import java.net.URL

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider : ProjectsProvider {
    private val _repositories: Iterable<Repository>

    constructor(url: URL) {
        val res = GitHubSearchQuery().byLink(url)
        _repositories = if (res != null) setOf(res) else emptySet()
    }

    constructor(searchCriteria: GitHubSearchCriteria) {
        _repositories = GitHubSearchQuery().byCriteria(searchCriteria)
    }

    override val repositories: Iterable<Repository>
        get() = _repositories
}
