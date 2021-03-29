package com.laamella.snippets_test_junit5;

import org.junit.jupiter.api.DynamicTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.walk;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Works together with JUnit5's @TestFactory to generate testcases
 * for all snippet files in a certain directory.
 *
 * @param <T> the processed test case type, produced by your testCaseProcessor.
 */
public class SnippetBatchTestFactory<T> {
    private final BasePath basePath;
    private final TestCaseFilenameFilter testCaseFilenameFilter;
    private final ExpectationFileFormat fileFormat;
    private final TestCaseProcessor<T> testCaseProcessor;
    private final ActualGenerator<T>[] actualGenerators;

    /**
     * @param fileFormat             the special strings used in the snippets.
     * @param basePath               sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included. {@link BasePath} will help in setting up this path.
     * @param testCaseFilenameFilter when only snippets are in the indicated directory, "path -> true"  is enough. Otherwise use something like "path -> path.toString().endsWith(".java")"
     * @param testCaseProcessor      takes the test case text that was found in a snippet file, and gives you the change to process it before passing the result to all the actualGenerators.
     * @param actualGenerators       the printers that create an "actual" to test against.
     */
    @SafeVarargs
    public SnippetBatchTestFactory(
            ExpectationFileFormat fileFormat,
            BasePath basePath,
            TestCaseFilenameFilter testCaseFilenameFilter,
            TestCaseProcessor<T> testCaseProcessor,
            ActualGenerator<T>... actualGenerators) {
        this.basePath = requireNonNull(basePath);
        this.testCaseFilenameFilter = requireNonNull(testCaseFilenameFilter);
        this.fileFormat = requireNonNull(fileFormat);
        this.testCaseProcessor = requireNonNull(testCaseProcessor);
        this.actualGenerators = requireNonNull(actualGenerators);
    }

    public Stream<DynamicTest> stream() throws IOException {
        return stream(false);
    }

    /**
     * Use this once instead of "stream" to rewrite all expectations to the current actuals.
     */
    public Stream<DynamicTest> regenerateAllExpectations() throws IOException {
        return stream(true);
    }

    private Stream<DynamicTest> stream(boolean regenerate) throws IOException {
        Path testCasesPath = basePath.toPath().toAbsolutePath();

        if (!Files.exists(testCasesPath)) {
            createDirectories(testCasesPath);
            fail("Created new testcases directory in " + testCasesPath);
        }

        return walk(testCasesPath)
                .filter(Files::isRegularFile)
                .filter(testCaseFilenameFilter::isTestCase)
                .map(testCasesPath::relativize)
                .map(filename -> dynamicTest(
                        filename.toString(),
                        testCasesPath.resolve(filename).toAbsolutePath().toUri(),
                        new SnippetTest<>(
                                testCasesPath.resolve(filename),
                                regenerate,
                                fileFormat,
                                testCaseProcessor,
                                actualGenerators)));
    }
}
