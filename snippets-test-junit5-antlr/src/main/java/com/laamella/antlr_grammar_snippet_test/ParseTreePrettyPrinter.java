package com.laamella.antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.core.RecursivePrinter;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.Trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Pretty print out a whole tree. {@link Trees#getNodeText} is used on the node payloads to get the
 * text for the nodes. (Derived from Trees.toStringTree(....))
 */
public class ParseTreePrettyPrinter<L extends Lexer, P extends Parser> implements Printer<L, P> {
    private final RecursivePrinter<Tree> recursivePrinter;

    public ParseTreePrettyPrinter(String indent) {
        recursivePrinter = new RecursivePrinter<>(indent);
    }

    private String printNode(Tree tree, Parser parser) {
        return Utils.escapeWhitespace(Trees.getNodeText(tree, parser), false);
    }

    private List<Tree> getChildNodes(Tree tree) {
        List<Tree> childNodes = new ArrayList<>();
        for (int i = 0; i < tree.getChildCount(); i++) {
            childNodes.add(tree.getChild(i));
        }
        return childNodes;
    }

    @Override
    public String print(L lexer, P parser, List<Token> tokenList, ParseTree tree, List<String> errors) {
        return recursivePrinter.print(
                tree,
                this::getChildNodes,
                t -> printNode(t, parser),
                true);
    }
}