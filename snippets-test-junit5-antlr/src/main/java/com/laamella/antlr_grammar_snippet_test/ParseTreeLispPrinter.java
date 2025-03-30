package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

/**
 * Antlr's built-in parse tree printer.
 */
public class ParseTreeLispPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    public String print(L lexer, P parser, List<Token> tokenList, ParseTree tree, List<String> errors) {
        return tree.toStringTree(parser);
    }
}
