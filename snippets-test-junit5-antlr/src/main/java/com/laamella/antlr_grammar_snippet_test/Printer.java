package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

public interface Printer<L extends Lexer, P extends Parser> {
    String generate(GrammarTestCase<L, P> testCase);
}
