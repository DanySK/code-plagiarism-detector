package org.danilopianini.plagiarismdetector

import org.danilopianini.plagiarismdetector.input.CLIConfigurator

/**
 * Code plagiarism detector entry point.
 */
fun main(args: Array<String>) {
    CLIConfigurator().sessionFrom(args.toList())()
}
