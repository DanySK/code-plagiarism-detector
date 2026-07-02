package org.danilopianini.plagiarismdetector.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly

class RepositoryContentTest : FunSpec() {
    private val githubRepo = GitHubRepository("tassiLuca", "test-app-for-code-plagiarism-detector")

    init {
        test("Check if returns all sources") {
            val suffixFileJavaPattern = Regex(".*.java$")
            githubRepo.getSources(suffixFileJavaPattern).count() shouldBeExactly EXPECTED_SOURCES
        }

        test("No sources meets the given pattern") {
            val suffixFileJavaPattern = Regex(".*.cs")
            githubRepo.getSources(suffixFileJavaPattern).count() shouldBeExactly 0
        }
    }

    companion object {
        private const val EXPECTED_SOURCES = 8
    }
}
