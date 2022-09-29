package org.danilopianini.plagiarismdetector.output

import org.danilopianini.plagiarismdetector.core.Result
import org.danilopianini.plagiarismdetector.core.detector.Match
import java.io.PrintWriter
import java.nio.file.Path
import java.time.LocalDateTime

/**
 * A very simple [ResultsExporter], which exports results on a plain file, inside [outputDirectory].
 */
class PlainFileExporter<in M : Match>(outputDirectory: Path) : FileExporter<M>(outputDirectory) {

    override fun export(results: Set<Result<M>>, output: PrintWriter) {
        printHeader(output)
        results.forEach { printBody(it, output) }
    }

    private fun printHeader(out: PrintWriter) {
        out.println("*".repeat(LINE_LENGTH))
        out.println("$FILE_PREAMBLE ${LocalDateTime.now()}")
        out.println("*".repeat(LINE_LENGTH))
    }

    private fun printBody(result: Result<M>, out: PrintWriter) {
        out.println("> Matches between ${result.submittedProject.name} and ${result.comparedProject.name}")
        result.comparisonResult
            .filter { it.matches.any() }
            .groupBy { it.similarity }
            .toSortedMap(Comparator.reverseOrder())
            .forEach {
                out.println(">> With similarity: ${it.key}")
                it.value.flatMap { it.matches }.forEach { formatDuplicatedSections(it, out) }
            }
    }

    private fun formatDuplicatedSections(match: M, out: PrintWriter) {
        val (ptn, txt) = match.formattedMatch
        out.println(">".repeat(LINE_LENGTH))
        out.println(ptn)
        out.println("<".repeat(LINE_LENGTH))
        out.println(txt)
        out.println(">".repeat(LINE_LENGTH))
        out.println()
    }

    companion object {
        private const val LINE_LENGTH = 120
        private const val FILE_PREAMBLE = "This is the generated report from plagiarism detector tool at "
    }
}
