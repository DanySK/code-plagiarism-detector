package org.danilopianini.plagiarismdetector.provider

private const val PR_BUILD_VARIABLE = "PR_BUILD"

fun isExecutingOnPullRequest() = System.getenv(PR_BUILD_VARIABLE) == "true"
