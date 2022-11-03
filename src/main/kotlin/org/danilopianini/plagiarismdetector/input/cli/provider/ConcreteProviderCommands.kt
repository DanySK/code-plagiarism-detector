package org.danilopianini.plagiarismdetector.input.cli.provider

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
