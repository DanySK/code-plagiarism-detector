package org.danilopianini.plagiarismdetector.analyzer.steps

import com.github.javaparser.ParserConfiguration
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import java.io.File

/**
 * A java source file parser.
 */
class JavaParser : StepHandler<File, CompilationUnit> {
    override fun process(input: File): CompilationUnit {
        val parserConfiguration = ParserConfiguration().setAttributeComments(false)
        StaticJavaParser.setConfiguration(parserConfiguration)
        return StaticJavaParser.parse(input)
    }
}
