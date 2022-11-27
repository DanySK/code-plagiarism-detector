package org.danilopianini.plagiarismdetector.utils

/**
 * An interface modeling a programming language.
 */
sealed interface Language {

    /**
     * The name of the language.
     */
    val name: String

    /**
     * The extensions of the files developed using this language.
     */
    val fileExtensions: Regex
}

/**
 * Java programming language.
 */
object Java : Language {
    override val name: String = "java"
    override val fileExtensions: Regex = Regex(".*.java$")
}
