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
 * @param <T> the processed test case type, produced by your testCaseProcessor.
 */
public class ExpectationFileTester<T> {
    private final BasePath basePath;
    private final ExpectationFileFormat fileFormat;
    private String extension;
    private final TestCaseProcessor<T> testCaseProcessor;
    private final ActualGenerator<T>[] actualGenerators;

    /**
     * @param fileFormat        the special strings used in the snippets.
     * @param basePath          sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included. {@link BasePath} will help in setting up this path.
     * @param testCaseProcessor takes the test case text that was found in a snippet file, and gives you the change to process it before passing the result to all the actualGenerators.
     * @param actualGenerators  the printers that create an "actual" to test against.
     */
    @SafeVarargs
    public ExpectationFileTester(
            ExpectationFileFormat fileFormat,
            BasePath basePath,
            String extension,
            TestCaseProcessor<T> testCaseProcessor,
            ActualGenerator<T>... actualGenerators) {
        this.basePath = requireNonNull(basePath);
        this.fileFormat = requireNonNull(fileFormat);
        this.extension = requireNonNull(extension);
        this.testCaseProcessor = requireNonNull(testCaseProcessor);
        this.actualGenerators = requireNonNull(actualGenerators);
    }

    public void test(T testCase) throws IOException {
        test(testCase, false);
    }

    /**
     * Use this once instead of "test" to rewrite all expectations to the current actuals.
     */
    public void regenerateExpectation(T testCase) throws IOException {
        test(testCase, true);
    }

    private void test(T testCase, boolean regenerate) throws IOException {
        Path testCasesPath = basePath.toPath().toAbsolutePath();

        if (!Files.exists(testCasesPath)) {
            createDirectories(testCasesPath);
            fail("Created new testcases directory in " + testCasesPath);
        }

        return walk(testCasesPath)
                .filter(Files::isRegularFile)
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
