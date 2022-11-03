package org.danilopianini.plagiarismdetector.input.cli.provider

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.choice
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
 * Base implementation for [ProviderCommands].
 */
sealed class BaseProviderCommand(
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
        require(url != null || criteria != null) { "At least one between url and criteria must be valued." }

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

    private class CriteriaOptions : OptionGroup("Options to specify for search by criteria") {

        val service by option(help = SERVICE_HELP_MSG)
            .choice(*SupportedOptions.services.map(HostingService::name).toTypedArray())
            .split(",")
            .required()

        val user by option(help = USER_HELP_MSG)
            .split(",")
            .required()

        val repositoryName by option(help = REPO_NAME_HELP_MSG)
            .split(",")

        companion object {
            private const val SERVICE_HELP_MSG = "The hosting service."
            private const val USER_HELP_MSG = "The hosting service username of the repositories owner to search."
            private const val REPO_NAME_HELP_MSG = "The name of the searched repositories."
        }
    }
}
