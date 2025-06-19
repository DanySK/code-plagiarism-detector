package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java

import com.github.javaparser.ParserConfiguration
import com.github.javaparser.ast.CompilationUnit
import java.io.File
import org.danilopianini.plagiarismdetector.core.analyzer.StepHandler
import org.slf4j.LoggerFactory

/**
 * A java source file parser.
 */
class JavaParser : StepHandler<File, CompilationUnit> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override operator fun invoke(input: File): CompilationUnit {
        val parserConfiguration =
            ParserConfiguration()
                .setAttributeComments(false)
                .setLanguageLevel(ParserConfiguration.LanguageLevel.CURRENT)
        val parser = com.github.javaparser.JavaParser(parserConfiguration)
        return parser
            .parse(input)
            .runCatching {
                if (!isSuccessful) {
                    logger.error(
                        "Errors occurred parsing ${input.path}: ${problems.joinToString { it.verboseMessage }}",
                    )
                }
                result.get()
            }.getOrElse { error("Parsing of ${input.path} failed completely") }
    }
}
