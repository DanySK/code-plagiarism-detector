package org.danilopianini.plagiarismdetector.input.cli.provider

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.options.validate
import org.danilopianini.plagiarismdetector.utils.BitBucket
import org.danilopianini.plagiarismdetector.utils.GitHub
import org.danilopianini.plagiarismdetector.utils.HostingService
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

    private val service by option(help = SERVICE_HELP_MSG)
        .split(",")
        .validate {
            it.forEach {
                require(it.contains(Regex("\\w+:\\w+(/.*)?"))) {
                    "$it is not compliant to `service-name:owner[/repo-query]` format."
                }
            }
        }

    /**
     * The repository [URL]s to use.
     */
    val url: List<URL>? by option(help = URL_HELP_MSG)
        .convert { URL(it) }
        .split(",")

    /**
     * Gets a sequence of configured [SearchCriteria], with all combinations of given options.
     */
    val criteria: Sequence<SearchCriteria<*, *>>? by lazy {
        if (service != null) {
            val services = service!!.map { it.substringBefore(":") }.map(SupportedOptions::serviceBy)
            val owners = service!!.map { it.substringAfter(":").substringBefore("/") }
            val repoNames = service!!.map { it.substringAfter("/", "") }
            services.zip(owners)
                .zip(repoNames) { a, b -> Triple(a.first, a.second, b) }
                .map { byCriteria(it.first, it.second, it.third) }
                .asSequence()
        } else {
            null
        }
    }

    private fun byCriteria(service: HostingService, user: String, repoName: String): SearchCriteria<*, *> =
        when (service) {
            GitHub -> ByGitHubUser(user).let { if (repoName.isNotEmpty()) ByGitHubName(repoName, it) else it }
            BitBucket -> ByBitbucketUser(user).let { if (repoName.isNotEmpty()) ByBitbucketName(repoName, it) else it }
        }

    override fun run() {
        if (url == null && service == null) {
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
    }
}
