package org.danilopianini.plagiarismdetector.provider.criteria

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.runBlocking
import org.danilopianini.plagiarismdetector.graphql.github.GetUserRepositoriesQuery
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A search criterion to filter repositories.
 */
interface GitHubGraphQLSearchCriteria : SearchCriteria<ApolloClient, Sequence<Repository>>

typealias Repos = GetUserRepositoriesQuery.Data?

/**
 * A search criterion to filter by username.
 * @property repoOwner the GitHub username.
 * @property repoFilter the filter to apply to the repository name.
 */
data class ByGitHubUserGraphQL(
    val repoOwner: String,
    val repoFilter: String,
) : GitHubGraphQLSearchCriteria {
    override operator fun invoke(client: ApolloClient): Sequence<Repository> {
        fun queryFor(page: String? = null): Repos =
            runBlocking {
                client.query(
                    GetUserRepositoriesQuery(
                        repoOwner,
                        page?.let { Optional.present(it) } ?: Optional.absent(),
                    ),
                ).execute().data
            }
        val emitter =
            generateSequence<Pair<String?, Repos>>(null to queryFor()) { (_, result) ->
                checkNotNull(result) {
                    "No result from GitHub GraphQL API for repository owner '$repoOwner'"
                }
                checkNotNull(result.repositoryOwner) {
                    "No repository owner named '$repoOwner'"
                }
                if (result.repositoryOwner.repositories.pageInfo.hasNextPage) {
                    val cursor = result.repositoryOwner.repositories.pageInfo.endCursor
                    cursor to queryFor(cursor)
                } else {
                    null
                }
            }
        val repoList =
            emitter.flatMap { (_, repoList) ->
                repoList?.repositoryOwner?.repositories?.nodes?.asSequence()?.filterNotNull().orEmpty()
            }
        return repoList
            .filter {
                it.name.contains(repoFilter)
            }
            .map { GitHubRepository(owner = repoOwner, name = it.name) }
    }
}
