package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.antlr.v4.runtime.Token.DEFAULT_CHANNEL;

public class TokensPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    private final boolean printHiddenTokens;

    public TokensPrinter(boolean printHiddenTokens) {
        this.printHiddenTokens = printHiddenTokens;
    }

    @Override
    public String generate(GrammarTestCase<L, P> testCase) {
        L lexer = testCase.getLexer();
        return testCase.getTokens().stream()
                .filter(token -> printHiddenTokens || token.getChannel() == DEFAULT_CHANNEL)
                .map(token -> ((CommonToken)token).toString(lexer).trim() + "\n")
                .collect(joining());
    }
}
