package org.danilopianini.plagiarismdetector.input.cli.provider

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.choice
import org.danilopianini.plagiarismdetector.commons.HostingService
import org.danilopianini.plagiarismdetector.input.SupportedOptions

/**
 * Command encapsulating submission provider options.
 */
class SubmissionProviderCommand : BaseProviderCommand(
    name = "submission",
    help = "Submission options."
)

/**
 * Command encapsulating corpus provider options.
 */
class CorpusProviderCommand : BaseProviderCommand(
    name = "corpus",
    help = "Corpus Options."
)

/**
 * Options for the criteria.
 */
class CriteriaOptions : OptionGroup("Options to specify for search by criteria") {

    /**
     * The hosting service.
     */
    val service by option(help = SERVICE_HELP_MSG)
        .choice(*SupportedOptions.services.map(HostingService::name).toTypedArray())
        .split(",")
        .required()

    /**
     * The username owner of the repos to search.
     */
    val user by option(help = USER_HELP_MSG)
        .split(",")
        .required()

    /**
     * The name of the searched repos.
     */
    val repositoryName by option(help = REPO_NAME_HELP_MSG)
        .split(",")

    companion object {
        private const val SERVICE_HELP_MSG = "The hosting service."
        private const val USER_HELP_MSG = "The hosting service username of the repositories owner to search."
        private const val REPO_NAME_HELP_MSG = "The name of the searched repositories."
    }
}
