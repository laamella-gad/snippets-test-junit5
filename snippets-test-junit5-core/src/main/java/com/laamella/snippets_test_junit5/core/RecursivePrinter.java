package com.laamella.snippets_test_junit5.core;

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Prints hierarchical data in a simple tree shape.
 * Utility class for users.
 */
public class RecursivePrinter<T> {
    private final String indent;

    public RecursivePrinter(String indent) {
        this.indent = requireNonNull(indent);
    }

    public String print(
            T node,
            Function<T, List<T>> getChildNodes,
            Function<T, String> printNode,
            boolean childfreeNodesOnOneLine) {
        StringBuilder output = new StringBuilder();
        print(node, output, 0, getChildNodes, printNode, childfreeNodesOnOneLine);
        return output.toString()
                .replaceAll("(?m)^\\s+$", "")
                .replaceAll("\\r?\\n\\r?\\n", "\n")
                .trim();
    }

    private void print(
            T t,
            StringBuilder output,
            int level,
            Function<T, List<T>> getChildNodes,
            Function<T, String> printNode,
            boolean childfreeNodesOnOneLine) {
        List<T> childNodes = getChildNodes.apply(t);
        if (childfreeNodesOnOneLine && childNodes.isEmpty()) {
            output.append(printNode.apply(t));
            return;
        }
        output
                .append(makeIndent(level))
                .append(printNode.apply(t))
                .append(' ');
        for (int i = 0; i < childNodes.size(); i++) {
            if (i != 0) {
                output.append(" ");
            }
            print(childNodes.get(i), output, level + 1, getChildNodes, printNode, childfreeNodesOnOneLine);
        }
        output.append(makeIndent(level));
    }

    private String makeIndent(int level) {
        StringBuilder sb = new StringBuilder();
        if (level > 0) {
            sb.append("\n");
            while (level > 0) {
                sb.append(indent);
                level--;
            }
        }
        return sb.toString();
    }
}
