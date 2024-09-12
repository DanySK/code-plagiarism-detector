package org.danilopianini.plagiarismdetector.input.cli

import com.github.ajalt.clikt.core.subcommands
import org.danilopianini.plagiarismdetector.input.RunConfigurator
import org.danilopianini.plagiarismdetector.input.SupportedOptions
import org.danilopianini.plagiarismdetector.input.cli.provider.CorpusProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.ProviderCommand
import org.danilopianini.plagiarismdetector.input.cli.provider.SubmissionProviderCommand
import org.danilopianini.plagiarismdetector.input.configuration.RunConfiguration
import org.danilopianini.plagiarismdetector.input.configuration.RunConfigurationImpl
import org.danilopianini.plagiarismdetector.output.Output
import org.danilopianini.plagiarismdetector.provider.BitbucketProvider
import org.danilopianini.plagiarismdetector.provider.GitHubGraphQLProvider
import org.danilopianini.plagiarismdetector.provider.GitHubRestProvider
import org.danilopianini.plagiarismdetector.provider.authentication.EnvironmentTokenSupplier
import org.danilopianini.plagiarismdetector.provider.criteria.BitbucketSearchCriteria
import org.danilopianini.plagiarismdetector.provider.criteria.GitHubGraphQLSearchCriteria
import org.danilopianini.plagiarismdetector.provider.criteria.GitHubRestSearchCriteria
import org.danilopianini.plagiarismdetector.provider.criteria.SearchCriteria
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.BitBucket
import org.danilopianini.plagiarismdetector.utils.GitHubGraphQL
import org.danilopianini.plagiarismdetector.utils.GitHubRest
import org.danilopianini.plagiarismdetector.utils.HostingService
import java.net.URL
import kotlin.system.exitProcess

/**
 * A concrete [RunConfigurator] which parses CLI arguments to create a new run configuration.
 */
class CLIConfigurator(private val output: Output) : RunConfigurator {

    private val githubRest: GitHubRestProvider by lazy {
        GitHubRestProvider.connectWithToken(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR))
    }

    private val githubGraphQL: GitHubGraphQLProvider by lazy {
        GitHubGraphQLProvider(EnvironmentTokenSupplier(GH_AUTH_TOKEN_VAR).token)
    }

    private val bitbucket: BitbucketProvider by lazy {
        BitbucketProvider.connectWithToken(
            EnvironmentTokenSupplier(BB_AUTH_USER_VAR, BB_AUTH_TOKEN_VAR, separator = ":"),
        )
    }

    override fun invoke(arguments: List<String>): RunConfiguration<*> {
        val submissionCommand = SubmissionProviderCommand()
        val corpusCommand = CorpusProviderCommand()
        val commonCommand = CLI(output)
        commonCommand.subcommands(submissionCommand, corpusCommand).main(arguments)
        output.logInfo("Retrieving projects...")
        val submission = repositoriesFrom(submissionCommand).also { output.logInfo("Found ${it.count()} submissions") }
        val corpus = repositoriesFrom(corpusCommand).also { output.logInfo("Found ${it.count()} corpus") }
        return RunConfigurationImpl(
            technique = commonCommand.techniqueType.facade,
            minDuplicationPercentage = commonCommand.minimumDuplication,
            submission = submission,
            corpus = corpus,
            filesToExclude = commonCommand.exclude?.toSet().orEmpty(),
            exporter = commonCommand.exporterType.exporter,
        )
    }

    private fun repositoriesFrom(configs: ProviderCommand): Set<Repository> {
        val url = runCatching { configs.url }
            .onFailure { exitProcessWithMessage(it.message ?: ERROR_MSG_MISSING_SUBCOMMANDS) }
            .getOrThrow()
        val criteria = runCatching { configs.criteria }
            .onFailure { exitProcessWithMessage(it.message ?: ERROR_MSG_MISSING_SUBCOMMANDS) }
            .getOrThrow()
        return url?.map { byLink(it, SupportedOptions.serviceBy(it)) }
            ?.toSet()
            ?: criteria?.flatMap { byCriteria(it) }?.toSet()
            ?: error("Neither url nor criteria are valued!")
    }

    private fun exitProcessWithMessage(message: String, exitStatus: Int = 1) {
        output.logInfo(message)
        exitProcess(exitStatus)
    }

    private fun byLink(link: URL, service: HostingService): Repository = when (service) {
        GitHubRest -> githubRest.byLink(link)
        GitHubGraphQL -> githubGraphQL.byLink(link)
        BitBucket -> bitbucket.byLink(link)
    }

    private fun byCriteria(criteria: SearchCriteria<*, *>): Sequence<Repository> = when (criteria) {
        is GitHubRestSearchCriteria -> githubRest.byCriteria(criteria)
        is BitbucketSearchCriteria -> bitbucket.byCriteria(criteria)
        is GitHubGraphQLSearchCriteria -> criteria(githubGraphQL.client)
        else -> error("The extracted criteria is not valid.")
    }

    private companion object {
        private const val BB_AUTH_USER_VAR = "BB_USER"
        private const val BB_AUTH_TOKEN_VAR = "BB_TOKEN"
        private const val GH_AUTH_TOKEN_VAR = "GH_TOKEN"
        private const val ERROR_MSG_MISSING_SUBCOMMANDS =
            "ERROR: `corpus` and `submission` subcommands are both required"
    }
}
