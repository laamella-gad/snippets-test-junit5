package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.antlr.v4.runtime.Token.DEFAULT_CHANNEL;

public class TokensPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    private final boolean printHiddenTokens;

    public TokensPrinter(boolean printHiddenTokens) {
        this.printHiddenTokens = printHiddenTokens;
    }

    @Override
    public String print(L lexer, P parser, List<Token> tokenList, ParseTree tree, List<String> errors) {
        return tokenList.stream()
                .filter(token -> printHiddenTokens || token.getChannel() == DEFAULT_CHANNEL)
                .map(token -> ((CommonToken) token).toString(lexer).trim() + "\n")
                .collect(joining());
    }
}
