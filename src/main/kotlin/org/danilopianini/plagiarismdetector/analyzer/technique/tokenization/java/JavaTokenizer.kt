package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.visitor.TreeVisitor
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.analyzer.StepHandler

/**
 * A Java source file tokenizer.
 */
class JavaTokenizer : StepHandler<CompilationUnit, Sequence<Token>> {
    override fun process(input: CompilationUnit): Sequence<Token> {
        val tokenizer = TokenizerTreeVisitor()
        tokenizer.visitPreOrder(input)
        return tokenizer.sourceCodeToken
    }

    /**
     * A generator of tokens, obtained by visiting the AST of the source file.
     */
    private class TokenizerTreeVisitor : TreeVisitor() {
        private val javaTokenTypes = JavaTokenTypesSupplier().types
        private val tokens = mutableListOf<Token>()

        /**
         * Sequence of [Token] extracted visiting the source AST.
         */
        val sourceCodeToken: Sequence<Token>
            get() {
                val result = tokens.toList()
                tokens.clear()
                return result.asSequence()
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
