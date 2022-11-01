package org.danilopianini.plagiarismdetector.input.cli.provider

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import org.danilopianini.plagiarismdetector.commons.BitBucket
import org.danilopianini.plagiarismdetector.commons.GitHub
import org.danilopianini.plagiarismdetector.commons.HostingService
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
    help: String,
) : ProviderCommands, CliktCommand(name = name, help = help, printHelpOnEmptyArgs = true) {

    private val githubProvider = GitHubProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
    private val bitbucketProvider = BitbucketProvider.connectWithToken(
        EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":")
    )
    private val url by option(help = URL_HELP_MSG)
        .convert { URL(it) }
        .split(",")
    private val criteria by CriteriaOptions()
        .cooccurring()

    override fun run() = checkInputs(url, criteria)

    override fun getRepositories(): Sequence<Repository> =
        try {
            (url?.let { getResult(it) } ?: emptySequence()) + (criteria?.let { getResult(it) } ?: emptySequence())
        } catch (e: IllegalStateException) {
            error("$commandName must be valued (${e.message})")
        }

    private fun checkInputs(url: List<URL>?, criteria: CriteriaOptions?) =
        check(url != null || criteria != null) { "At least one between url and criteria must be valued." }

    private fun getResult(url: List<URL>): Sequence<Repository> = url
        .map { byLink(it, getServiceByUrl(it)) }
        .asSequence()

    private fun byLink(link: URL, service: HostingService) = when (service) {
        GitHub -> githubProvider.byLink(link)
        BitBucket -> bitbucketProvider.byLink(link)
    }

    private fun getResult(criteria: CriteriaOptions): Sequence<Repository> =
        criteria.user
            .asSequence()
            .flatMap { u ->
                criteria.service.flatMap { s ->
                    (criteria.repositoryName ?: emptyList()).flatMap { n ->
                        byCriteria(getServiceByName(s), u, n)
                    }
                }
            }

    private fun byCriteria(service: HostingService, user: String, repositoryName: String?) = when (service) {
        GitHub -> githubProvider.byCriteria(
            repositoryName?.let { ByGitHubName(it, ByGitHubUser(user)) } ?: ByGitHubUser(user)
        )
        BitBucket -> bitbucketProvider.byCriteria(
            repositoryName?.let { ByBitbucketName(it, ByBitbucketUser(user)) } ?: ByBitbucketUser(user)
        )
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
