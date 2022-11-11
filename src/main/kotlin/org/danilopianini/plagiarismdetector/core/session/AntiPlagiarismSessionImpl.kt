package org.danilopianini.plagiarismdetector.core.session

import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.input.configuration.RunConfiguration
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.system.measureTimeMillis

/**
 * A **parallel** implementation of an [AntiPlagiarismSession].
 */
class AntiPlagiarismSessionImpl<out C : RunConfiguration<M>, M : Match>(
    private val configuration: C
) : AntiPlagiarismSession {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override operator fun invoke() = logExecutionTime {
        with(configuration) {
            submission.forEach { submission ->
                val result = corpus
                    .filter { it.name != submission.name }
                    .toSet()
                    .parallelStream()
                    .map { technique.execute(submission, it, filesToExclude, minDuplicatedPercentage) }
                    .collect(Collectors.toSet())
                if (result.isNotEmpty()) {
                    configuration.exporter(result)
                }
            }
        }
    }

    private fun logExecutionTime(block: () -> Unit) {
        val elapsedTime = measureTimeMillis(block)
        logger.info("> Elapsed time: ${elapsedTime.toDouble() / MILLISECONDS_IN_ONE_MINUTE} min.")
    }

    companion object {
        private const val MILLISECONDS_IN_ONE_MINUTE = 60_000
    }
}
