package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import org.danilopianini.plagiarismdetector.core.analyzer.StepHandler

/**
 * A java source file preprocessor, which removes noisy information
 * (import and packages declarations) that can affect the overall detection
 * accuracy of the system.
 */
class JavaPreprocessor : StepHandler<CompilationUnit, CompilationUnit> {

    override operator fun invoke(input: CompilationUnit): CompilationUnit = input.clone().also {
        it.removePackageDeclaration()
        it.removeImports()
        it.removeEqualsAndHashCodeFunctions()
    }

    private fun CompilationUnit.removeImports() {
        findAll(ImportDeclaration::class.java)
            .asSequence()
            .forEach(this::remove)
    }

    private fun CompilationUnit.removeEqualsAndHashCodeFunctions() {
        findAll(MethodDeclaration::class.java)
            .asSequence()
            .filter { it.name.asString() == "equals" || it.name.asString() == "hashCode" }
            .map { it.parentNode }
            .forEach { it.ifPresent(this::remove) }
    }
}
