package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.subcommands
import org.danilopianini.plagiarismdetector.commons.BitBucket
import org.danilopianini.plagiarismdetector.commons.GitHub
import org.danilopianini.plagiarismdetector.commons.HostingService
import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSession
import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSessionImpl
import org.danilopianini.plagiarismdetector.input.RunConfigurator
import org.danilopianini.plagiarismdetector.input.SupportedOptions
import org.danilopianini.plagiarismdetector.input.cli.provider.ProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.CorpusProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.SubmissionProviderCommand
import org.danilopianini.plagiarismdetector.input.configuration.RunConfigurationImpl
import org.danilopianini.plagiarismdetector.provider.BitbucketProvider
import org.danilopianini.plagiarismdetector.provider.GitHubProvider
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier
import org.danilopianini.plagiarismdetector.provider.criteria.BitbucketSearchCriteria
import org.danilopianini.plagiarismdetector.provider.criteria.GitHubSearchCriteria
import org.danilopianini.plagiarismdetector.provider.criteria.SearchCriteria
import org.danilopianini.plagiarismdetector.repository.Repository
import java.net.URL

/**
 * A concrete [RunConfigurator] which parses CLI arguments to create a new run configuration.
 */
class CLIConfigurator : RunConfigurator {

    private val githubProvider by lazy {
        GitHubProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
    }
    private val bitbucketProvider by lazy {
        BitbucketProvider.connectWithToken(
            EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":")
        )
    }

    override fun sessionFrom(arguments: List<String>): AntiPlagiarismSession {
        val submissionCommand = SubmissionProviderCommand()
        val corpusCommand = CorpusProviderCommand()
        val commonCommand = CLI()
        commonCommand.subcommands(submissionCommand, corpusCommand).main(arguments)
        val config = RunConfigurationImpl(
            technique = commonCommand.techniqueType.facade,
            minDuplicatedPercentage = commonCommand.minimumDuplication,
            submission = repositoriesFrom(submissionCommand),
            corpus = repositoriesFrom(corpusCommand),
            filesToExclude = commonCommand.exclude?.toSet() ?: emptySet(),
            exporter = commonCommand.exporterType.exporter
        )
        return AntiPlagiarismSessionImpl(config)
    }

    private fun repositoriesFrom(configs: ProviderCommand): Set<Repository> = try {
        with(configs) {
            url?.map { byLink(it, serviceBy(it)) }?.toSet() ?: criteria?.flatMap { byCriteria(it) }?.toSet() ?: error(
                "Neither url nor criteria are valued!"
            )
        }
    } catch (e: IllegalStateException) {
        throw IllegalArgumentException("Both `corpus` and `provider` subcommands are required \n$e")
    }

    private fun byLink(link: URL, service: HostingService): Repository = when (service) {
        GitHub -> githubProvider.byLink(link)
        BitBucket -> bitbucketProvider.byLink(link)
    }

    private fun byCriteria(criteria: SearchCriteria<*, *>): Set<Repository> = runCatching {
        when (criteria) {
            is GitHubSearchCriteria -> githubProvider.byCriteria(criteria).toSet()
            is BitbucketSearchCriteria -> bitbucketProvider.byCriteria(criteria).toSet()
            else -> error("The extracted criteria is not valid.")
        }
    }.getOrElse { emptySet() }

    private fun serviceBy(url: URL): HostingService =
        SupportedOptions.services.find { it.host == url.host } ?: error("${url.host} not supported!")

    companion object {
        private const val BB_AUTH_USER_VAR = "BB_USER"
        private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
    }
}
