package org.danilopianini.plagiarismdetector.input.cli.provider

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
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
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketName
import org.danilopianini.plagiarismdetector.provider.criteria.ByBitbucketUser
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubName
import org.danilopianini.plagiarismdetector.provider.criteria.ByGitHubUser
import org.danilopianini.plagiarismdetector.provider.criteria.SearchCriteria
import java.net.URL

/**
 * An abstract class encapsulating repository provider configuration.
 */
sealed class ProviderCommand(
    name: String,
    help: String,
) : CliktCommand(name = name, help = help) {

    private val criteriaOptions by CriteriaOptions()
        .cooccurring()

    /**
     * The repository [URL]s to use.
     */
    val url by option(help = URL_HELP_MSG)
        .convert { URL(it) }
        .split(",")

    /**
     * The [SearchCriteria] to use.
     */
    val criteria by lazy {
        criteriaOptions?.criteria
    }

    override fun run() {
        if (url == null && criteriaOptions == null) {
            throw PrintMessage(
                message = "At least one between `url` and `criteria` must be valued in `$commandName` command.",
                error = true
            )
        }
    }

    companion object {
        private const val MORE_ARGS_HELP = "possibly separated by commas"
        private const val URL_HELP_MSG = "The URL addresses of the repositories to be retrieved, $MORE_ARGS_HELP."
        private const val SERVICE_HELP_MSG = "The hosting services where are stored the repositories, $MORE_ARGS_HELP."
        private const val USER_HELP_MSG = "The usernames of the repos owners, $MORE_ARGS_HELP."
        private const val REPO_NAME_HELP_MSG = "The names of the searched repositories, $MORE_ARGS_HELP."
    }

    private inner class CriteriaOptions : OptionGroup("Options to specify for search by criteria") {

        private val service by option(help = SERVICE_HELP_MSG)
            .choice(*SupportedOptions.services.map(HostingService::name).toTypedArray())
            .split(",")
            .required()
        private val user by option(help = USER_HELP_MSG)
            .split(",")
            .required()
        private val repositoryName by option(help = REPO_NAME_HELP_MSG)
            .split(",")
            .required()

        /**
         * Gets a sequence of configured [SearchCriteria], with all combinations of given options.
         */
        val criteria: Sequence<SearchCriteria<*, *>> by lazy {
            service.map(SupportedOptions::serviceBy)
                .flatMap { s -> user.flatMap { u -> repositoryName.map { byCriteria(s, u, it) } } }
                .asSequence()
        }

        private fun byCriteria(service: HostingService, user: String, repositoryName: String?): SearchCriteria<*, *> =
            when (service) {
                GitHub -> repositoryName?.let { ByGitHubName(it, ByGitHubUser(user)) } ?: ByGitHubUser(user)
                BitBucket -> repositoryName?.let { ByBitbucketName(it, ByBitbucketUser(user)) } ?: ByBitbucketUser(user)
            }
    }
}
