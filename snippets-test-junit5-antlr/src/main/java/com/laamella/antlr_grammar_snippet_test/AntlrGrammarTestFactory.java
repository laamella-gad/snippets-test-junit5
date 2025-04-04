package com.laamella.antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.laamella.snippets_test_junit5.core.TestCase.multiOutputTestCase;
import static org.antlr.v4.runtime.atn.PredictionMode.LL_EXACT_AMBIG_DETECTION;

/**
 * Works together with JUnit5's @{@link org.junit.jupiter.api.TestFactory} to generate testcases
 * for all the Antlr grammar test case snippets in a directory.
 *
 * @param <L> the lexer you want to test
 * @param <P> the parser you want to test.
 */
public class AntlrGrammarTestFactory<L extends Lexer, P extends Parser> extends SnippetTestFactory {
    /**
     * @param lexerFactory           creates a new lexer for your grammar
     * @param parserFactory          creates a new parser for your grammar
     * @param mainRule               the main parse rule to invoke (like FortranParser::forLoop)
     * @param basePath               sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included.
     * @param testCaseFilenameFilter when only snippets are in the indicated directory, "path -> true"  is enough. Otherwise use something like "path -> path.toString().endsWith(".java")"
     * @param printers               the printers that create an "actual" to test against. {@link ParseTreePrettyPrinter} and {@link ErrorsPrinter} are recommended.
     */
    @SafeVarargs
    public AntlrGrammarTestFactory(
            LexerFactory<L> lexerFactory,
            ParserFactory<P> parserFactory,
            MainRule<P> mainRule, String blockCommentOpen,
            String blockCommentClose,
            BasePath basePath,
            TestCaseFilenameFilter testCaseFilenameFilter,
            Printer<L, P>... printers) {
        super(
                new SnippetFileFormat(
                        blockCommentOpen,
                        blockCommentClose + "\n",
                        "\n---\n",
                        "\n" + blockCommentOpen + " expected:\n",
                        "\n---\n",
                        blockCommentClose),
                basePath,
                testCaseFilenameFilter,
                multiOutputTestCase(testCaseText -> runTest(testCaseText, mainRule, lexerFactory, parserFactory, printers))
        );
    }

    @SafeVarargs
    private static <L extends Lexer, P extends Parser> List<String> runTest(
            String testCaseText,
            MainRule<P> mainRule,
            LexerFactory<L> lexerFactory,
            ParserFactory<P> parserFactory,
            Printer<L, P>... printers) {
        List<String> errors = new ArrayList<>();
        L lexer = lexerFactory.create(CharStreams.fromString(testCaseText));
        collectErrorAndWarningMessagesInList(lexer, errors);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        List<Token> tokenList = tokens.getTokens();
        P parser = parserFactory.create(tokens);
        collectErrorAndWarningMessagesInList(parser, errors);
        parser.getInterpreter().setPredictionMode(LL_EXACT_AMBIG_DETECTION);
        ParseTree tree = mainRule.invoke(parser);
        return Arrays.stream(printers).map(p -> p.print(lexer, parser, tokenList, tree, errors)).collect(Collectors.toList());
    }

    private static void collectErrorAndWarningMessagesInList(Recognizer<?, ?> recognizer, List<String> errors) {
        recognizer.removeErrorListeners();
        recognizer.addErrorListener(new DiagnosticErrorListener(true));
        recognizer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errors.add("line: " + line + ", " +
                        "offset: " + charPositionInLine + ", " +
                        (offendingSymbol != null ? "symbol:" + offendingSymbol + " " : "") +
                        msg);
            }
        });
    }
}
