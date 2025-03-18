package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

/**
 * Prints a list of error messages encountered during parsing.
 */
public class ErrorsPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    @Override
    public String print(L lexer, P parser, List<Token> tokenList, ParseTree tree, List<String> errors) {
        if (errors.isEmpty()) {
            return "No errors.";
        }
        return String.join("\n", errors);
    }
}
