package org.danilopianini.plagiarismdetector.input.cli

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir

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
})
