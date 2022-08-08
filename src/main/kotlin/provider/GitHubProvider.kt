package provider

import java.net.URL

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider : ProjectsProvider {
    private val _repositories: Iterable<Repository>

    constructor(url: URL) {
        _repositories = GitHubSearchQuery().byLink(url).result
    }

    constructor(repositoryName: String, user: String) {
        _repositories = GitHubSearchQuery()
            .byCriteria()
            .byName(repositoryName)
            .byUser(user)
            .search()
            .result
    }

    override val repositories: Iterable<Repository>
        get() = _repositories
}
