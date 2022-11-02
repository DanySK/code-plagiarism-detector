package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java

import com.github.javaparser.ParserConfiguration
import com.github.javaparser.ast.CompilationUnit
import org.danilopianini.plagiarismdetector.core.analyzer.StepHandler
import java.io.File

/**
 * A java source file parser.
 */
class JavaParser : StepHandler<File, CompilationUnit> {

    override operator fun invoke(input: File): CompilationUnit {
        val parserConfiguration = ParserConfiguration().setAttributeComments(false)
        val parser = com.github.javaparser.JavaParser(parserConfiguration)
        return parser.parse(input).run {
            check(isSuccessful) { "Errors occurred parsing ${input.path}: ${problems.joinToString { it.message }}" }
            result.get()
        }
    }
}
