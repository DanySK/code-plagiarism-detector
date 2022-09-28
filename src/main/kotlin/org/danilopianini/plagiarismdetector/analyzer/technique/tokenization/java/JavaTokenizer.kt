package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.visitor.TreeVisitor
import org.danilopianini.plagiarismdetector.analyzer.StepHandler
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.FileTokenTypesSupplier

/**
 * A Java source file tokenizer.
 */
class JavaTokenizer : StepHandler<CompilationUnit, List<Token>> {
    companion object {
        private const val CONFIG_FILE_NAME = "java-token-types.yml"
    }

    override fun invoke(input: CompilationUnit): List<Token> = TokenizerTreeVisitor().run { visit(input) }

    /**
     * A generator of tokens, obtained by visiting the AST of the source file.
     */
    private class TokenizerTreeVisitor : TreeVisitor() {
        private val javaTokenTypes = FileTokenTypesSupplier(CONFIG_FILE_NAME).types
        private val tokens = mutableListOf<Token>()

        /**
         * Visit the AST generating the tokens for the given [CompilationUnit].
         * @param compilationUnit the [CompilationUnit] to visit and tokenize.
         * @return the [List] of [Token]s generated.
         */
        fun visit(compilationUnit: CompilationUnit): List<Token> {
            tokens.clear()
            visitPreOrder(compilationUnit)
            return tokens
        }

        override fun process(node: Node?) {
            tokenize(node)
        }

        private fun tokenize(node: Node?) {
            check(node != null)
            val tokenTypeName = node::class.java.simpleName
            if (javaTokenTypes.isToken(tokenTypeName)) {
                tokens.add(
                    TokenImpl(
                        node.begin.get().line,
                        node.begin.get().column,
                        javaTokenTypes.tokenFor(tokenTypeName)
                    )
                )
            }
        }
    }
}
