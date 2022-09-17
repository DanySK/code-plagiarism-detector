package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java

import com.github.javaparser.ParserConfiguration
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import org.danilopianini.plagiarismdetector.analyzer.StepHandler
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
