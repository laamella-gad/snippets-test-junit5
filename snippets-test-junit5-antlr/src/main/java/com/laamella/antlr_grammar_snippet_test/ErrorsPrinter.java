package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import java.util.List;

/**
 * Prints a list of error messages encountered during parsing.
 */
public class ErrorsPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    @Override
    public String generate(GrammarTestCase<L, P> testCase) {
        if (testCase.getErrors().isEmpty()) {
            return "No errors.";
        }
        return String.join("\n", testCase.getErrors());
    }
}
