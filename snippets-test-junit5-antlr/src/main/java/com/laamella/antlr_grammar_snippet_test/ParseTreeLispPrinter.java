package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

/**
 * Antlr's built-in parse tree printer.
 */
public class ParseTreeLispPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    public String generate(GrammarTestCase<L, P> testCase) {
        return testCase.getParseTree().toStringTree(testCase.getParser());
    }
}
