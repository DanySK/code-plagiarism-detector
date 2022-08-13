package provider

import java.net.URL

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider : ProjectsProvider {
    private val _repositories: Iterable<Repository>

    constructor(url: URL) {
        _repositories = GitHubSearchQuery().byLink(url)
    }

    constructor(searchCriteria: GitHubSearchCriteria) {
        _repositories = GitHubSearchQuery().byCriteria(searchCriteria)
    }

    override val repositories: Iterable<Repository>
        get() = _repositories
}
