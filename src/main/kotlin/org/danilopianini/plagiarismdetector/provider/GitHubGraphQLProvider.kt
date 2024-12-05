package org.danilopianini.plagiarismdetector.provider

import com.apollographql.apollo3.ApolloClient
import org.danilopianini.plagiarismdetector.provider.criteria.GitHubGraphQLSearchCriteria
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A provider of GitHub repositories.
 */
class GitHubGraphQLProvider(
    token: String?,
) : AbstractGitHubProvider<ApolloClient, Sequence<Repository>, GitHubGraphQLSearchCriteria>() {
    /**
     * The [ApolloClient] to use to connect to GitHub.
     */
    val client =
        ApolloClient.Builder()
            .serverUrl("https://api.github.com/graphql")
            .apply {
                if (token != null) {
                    addHttpHeader("Authorization", "Bearer $token")
                }
            }
            .build()

    override fun byCriteria(criteria: GitHubGraphQLSearchCriteria): Sequence<Repository> = criteria(client)
}
