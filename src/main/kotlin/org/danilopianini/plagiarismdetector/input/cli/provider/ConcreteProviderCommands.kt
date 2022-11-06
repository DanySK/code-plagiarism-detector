package org.danilopianini.plagiarismdetector.input.cli.provider

/**
 * Command encapsulating submission provider options.
 */
class SubmissionProviderCommand : ProviderCommand(
    name = "submission",
    help = "Submission options."
)

/**
 * Command encapsulating corpus provider options.
 */
class CorpusProviderCommand : ProviderCommand(
    name = "corpus",
    help = "Corpus Options."
)
