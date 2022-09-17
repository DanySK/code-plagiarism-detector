package org.danilopianini.plagiarismdetector.analyzer.steps.tokenization.java

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.ImportDeclaration
import org.danilopianini.plagiarismdetector.analyzer.steps.StepHandler

/**
 * A java source file preprocessor, which removes noisy information
 * (import and packages declarations) that can affect the overall detection
 * accuracy of the system.
 */
class JavaPreprocessor : StepHandler<CompilationUnit, CompilationUnit> {
    override fun process(input: CompilationUnit): CompilationUnit {
        input.removePackageDeclaration()
        input.findAll(ImportDeclaration::class.java).forEach(input::remove)
        return input
    }
}
