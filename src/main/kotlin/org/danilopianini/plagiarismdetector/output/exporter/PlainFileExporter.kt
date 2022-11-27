package org.danilopianini.plagiarismdetector.output.exporter

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.Output
import java.io.PrintWriter
import java.nio.file.Path
import java.time.LocalDateTime

/**
 * A very simple [ReportsExporter], which exports results on a plain file, inside [outputDirectory].
 */
class PlainFileExporter<in M : Match>(
    outputDirectory: Path,
    output: Output,
) : FileExporter<M>(outputDirectory, output) {

    override val fileExtension: String = "txt"

    override fun export(reports: Set<Report<M>>, output: PrintWriter) {
        printHeader(output)
        printSummary(reports, output)
        reports.asSequence()
            .filter { it.similarity > LOWER_BOUND_SIMILARITY }
            .toSortedSet(compareByDescending { it.similarity })
            .forEach { printBody(it, output) }
    }

    private fun printHeader(out: PrintWriter) {
        out.println("*".repeat(LINE_LENGTH))
        out.println("$FILE_PREAMBLE ${LocalDateTime.now()}")
        out.println("*".repeat(LINE_LENGTH))
    }

    private fun printSummary(reports: Set<Report<M>>, output: PrintWriter) {
        output.println()
        output.format("%-${LINE_LENGTH}s%n", "Submitted Project: ${reports.first().submittedProject}")
        output.println()
        output.println("${"+".repeat(SHORT_LINE_LENGTH)} SUMMARY  ${"+".repeat(SHORT_LINE_LENGTH)}")
        output.println("Compared with ${reports.count()} repositories.")
        output.println("-".repeat(LINE_LENGTH))
        output.format("|%-107s|%s|%n", "compared with", "similarity")
        output.println("-".repeat(LINE_LENGTH))
        reports.sortedByDescending { it.similarity }.forEach {
            output.format("|%-107s|%10.2f|%n", it.comparedProject.name, it.similarity)
        }
        output.println("-".repeat(LINE_LENGTH))
        output.println("+".repeat(LINE_LENGTH))
        output.println()
    }

    private fun printBody(report: Report<M>, out: PrintWriter) {
        out.println("> Matches found with ${report.comparedProject.name}")
        report.comparisonResult
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
        private const val LOWER_BOUND_SIMILARITY = 0.4
        private const val LINE_LENGTH = 120
        private const val SHORT_LINE_LENGTH = 55
        private const val FILE_PREAMBLE = "This is the generated report from plagiarism detector tool at"
    }
}
