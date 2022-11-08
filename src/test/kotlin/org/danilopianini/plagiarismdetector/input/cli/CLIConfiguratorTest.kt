package org.danilopianini.plagiarismdetector.input.cli

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.string.shouldStartWith

class CLIConfiguratorTest : FunSpec({

    test(
        "Launching CLI configuration with multiple users which are not" +
            " present in both services should not throw an exception"
    ) {
        val args = listOf(
            "--output-dir",
            tempdir().path,
            "submission",
            "--url",
            "https://github.com/DanySK/code-plagiarism-detector",
            "corpus",
            "--repository-name",
            "oop",
            "--user",
            "danysk,unibo-oop-projects",
            "--service",
            "github,bitbucket"
        )
        CLIConfigurator().sessionFrom(args)
    }

    test(
        "If one between `corpus` and `provider` command is not provided, " +
            "an `IllegalArgument` exception with explanatory message is thrown"
    ) {
        val args = listOf(
            "--output-dir",
            tempdir().path,
            "submission",
            "--url",
            "https://github.com/DanySK/code-plagiarism-detector",
        )
        val exception = shouldThrow<IllegalArgumentException> {
            CLIConfigurator().sessionFrom(args)
        }
        exception.message shouldStartWith "Both `corpus` and `provider` subcommands are required"
    }
})
