package org.danilopianini.plagiarismdetector.input.cli.provider

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import org.danilopianini.plagiarismdetector.commons.BitBucket
import org.danilopianini.plagiarismdetector.commons.GitHub
import org.danilopianini.plagiarismdetector.input.SupportedOptions
import org.danilopianini.plagiarismdetector.provider.BitbucketProvider
import org.danilopianini.plagiarismdetector.provider.GitHubProvider
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketName
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketUser
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubName
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUser
import org.danilopianini.plagiarismdetector.repository.Repository
import java.net.URL

/**
 * An interface modeling commands used for configuring repository provider.
 */
interface ProviderCommands {

    /**
     * Returns a [Sequence] of [Repository] containing the ones
     * taken from the configured provider.
     */
    fun getRepositories(): Sequence<Repository>
}

/**
 * Base implementation for [ProviderCommands].
 */
abstract class BaseProviderCommand(
    name: String,
    help: String
) : ProviderCommands, CliktCommand(name = name, help = help) {

    private val githubProvider = GitHubProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
    private val bitbucketProvider = BitbucketProvider.connectWithToken(
        EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":")
    )
    private val url by option(help = URL_HELP_MSG)
        .convert { URL(it) }
    private val criteria by CriteriaOptions()
        .cooccurring()

    override fun run() = checkInputs(url, criteria)

    override fun getRepositories(): Sequence<Repository> =
        url?.let { sequenceOf(getResult(it)) } ?: criteria?.let { getResult(it) } ?: error("Error")

    private fun checkInputs(url: URL?, criteria: CriteriaOptions?) {
        check((url != null || criteria != null) && !(url != null && criteria != null)) {
            "Exactly one between url and criteria must be valued."
        }
    }

    private fun getResult(url: URL): Repository {
        return when (getServiceByUrl(url)) {
            GitHub -> githubProvider.byLink(url)
            BitBucket -> bitbucketProvider.byLink(url)
        }
    }

    private fun getResult(criteria: CriteriaOptions): Sequence<Repository> {
        return when (getServiceByName(criteria.service)) {
            GitHub -> githubProvider.byCriteria(
                criteria.repositoryName?.let {
                    ByGitHubName(it, ByGitHubUser(criteria.user))
                } ?: ByGitHubUser(criteria.user)
            )
            BitBucket -> bitbucketProvider.byCriteria(
                criteria.repositoryName?.let {
                    ByBitbucketName(it, ByBitbucketUser(criteria.user))
                } ?: ByBitbucketUser(criteria.user)
            )
        }
    }

    private fun getServiceByUrl(url: URL) =
        SupportedOptions.services.find { it.host == url.host } ?: error("${url.host} not supported!")

    private fun getServiceByName(serviceName: String) =
        SupportedOptions.services.find { it.name == serviceName } ?: error("$serviceName not supported!")

    companion object {
        private const val URL_HELP_MSG = "The URL address of the repository."
        private const val BB_AUTH_USER_VAR = "BB_USER"
        private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
    }
}
