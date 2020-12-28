package com.laamella.snippets_test_junit5;

import org.junit.jupiter.api.DynamicTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.walk;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Works together with JUnit5's @TestFactory to generate testcases
 * for all snippet files in a certain directory.
 *
 * @param <T> the processed test case type, produced by your testCaseProcessor.
 */
public class SnippetTestFactory<T> {
    private final String descriptionStartString;
    private final String descriptionEndString;
    private final String separatorAfterTestCase;
    private final String separatorBetweenExpectations;
    private final String expectationsEndString;
    private final BasePath basePath;
    private final Predicate<Path> testcaseFilenameFilter;
    private final Function<String, T> testCaseProcessor;
    private final ActualGenerator<T>[] actualGenerators;

    /**
     * @param basePath                     sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included. {@link BasePath} will help in setting up this path.
     * @param testcaseFilenameFilter       when only snippets are in the indicated directory, "path -> true"  is enough. Otherwise use something like "path -> path.toString().endsWith(".java")"
     * @param testCaseProcessor            takes the test case text that was found in a snippet file, and gives you the change to process it before passing the result to all the actualGenerators.
     * @param descriptionStartString       the string that indicates the start of the optional description. This has to be the very first piece of text to appear in the snippet, or it is assumed that there is no description.
     * @param descriptionEndString         the string that indicates the end of the optional description. You probably want to end it with a \n
     * @param separatorAfterTestCase       the separator that comes right after the test case. If it is not found, it is assumed that no expectation is in the snippet (you probably just created a new snippet here), and the "actual" is written to the snippet file. Don't forget to include \n if you want new lines.
     * @param separatorBetweenExpectations the separator that goes between the expectations in the file. They come in the same order as the actualGenerators. Don't forget to include \n if you want new lines.
     * @param expectationsEndString        the last piece of text in the file. Don't forget to include \n if you want new lines.
     * @param actualGenerators             the printers that create an "actual" to test against.
     */
    @SafeVarargs
    public SnippetTestFactory(
            BasePath basePath,
            Predicate<Path> testcaseFilenameFilter,
            Function<String, T> testCaseProcessor,
            String descriptionStartString, String descriptionEndString, String separatorAfterTestCase,
            String separatorBetweenExpectations,
            String expectationsEndString,
            ActualGenerator<T>... actualGenerators) {
        this.basePath = requireNonNull(basePath);
        this.testcaseFilenameFilter = requireNonNull(testcaseFilenameFilter);
        this.testCaseProcessor = requireNonNull(testCaseProcessor);
        this.actualGenerators = requireNonNull(actualGenerators);
        this.separatorAfterTestCase = requireNonNull(separatorAfterTestCase);
        this.separatorBetweenExpectations = requireNonNull(separatorBetweenExpectations);
        this.expectationsEndString = requireNonNull(expectationsEndString);
        this.descriptionStartString = requireNonNull(descriptionStartString);
        this.descriptionEndString = requireNonNull(descriptionEndString);
    }

    public Stream<DynamicTest> stream(String testCasesDirectory) throws IOException {
        return stream(testCasesDirectory, false);
    }

    /**
     * Use this once instead of "stream" to rewrite all expectations to the current actuals.
     */
    public Stream<DynamicTest> regenerate(String testCasesDirectory) throws IOException {
        return stream(testCasesDirectory, true);
    }

    public Stream<DynamicTest> stream(String testCasesDirectory, boolean regenerate) throws IOException {
        Path testCasesPath = basePath.toPath().resolve(testCasesDirectory).toAbsolutePath();

        createDirectories(testCasesPath);

        return walk(testCasesPath)
                .filter(Files::isRegularFile)
                .filter(testcaseFilenameFilter)
                .map(testCasesPath::relativize)
                .map(filename -> dynamicTest(
                        filename.toString(),
                        testCasesPath.resolve(filename).toAbsolutePath().toUri(),
                        new SnippetTest<>(
                                testCasesPath.resolve(filename),
                                regenerate,
                                descriptionStartString,
                                descriptionEndString,
                                separatorAfterTestCase,
                                separatorBetweenExpectations,
                                expectationsEndString,
                                testCaseProcessor,
                                actualGenerators)));
    }
}
