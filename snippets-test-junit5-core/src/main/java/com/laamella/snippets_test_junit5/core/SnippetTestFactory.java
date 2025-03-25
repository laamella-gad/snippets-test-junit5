package com.laamella.snippets_test_junit5.core;

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
 */
public class SnippetTestFactory {
    private final SnippetFileFormat fileFormat;
    private final BasePath basePath;
    private final TestCaseFilenameFilter testCaseFilenameFilter;
    private final TestCase testCase;

    /**
     * The factory will generate test cases for all files matching testCaseFilenameFilter in directory basePath and its subdirectories.
     * The files will be read and interpreted according to the fileFormat.
     * The input in the files will be passed through the testCaseProcessor so that the plain text can be turned into the model you want to work with.
     * That model is passed to each of the actualGenerators, which create the "actuals" that will be compared with the "expectations" in the file.
     *
     * @param fileFormat             the special strings used in the snippets.
     * @param basePath               sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included. {@link BasePath} will help in setting up this path.
     * @param testCaseFilenameFilter when only snippets are in the indicated directory, "path -> true"  is enough. Otherwise use something like "path -> path.toString().endsWith(".java")"
     * @param testCase      takes the test case text that was found in a snippet file, runs your test, and outputs the "actuals".
     */
    public SnippetTestFactory(
            SnippetFileFormat fileFormat,
            BasePath basePath,
            TestCaseFilenameFilter testCaseFilenameFilter,
            TestCase testCase) {
        this.basePath = requireNonNull(basePath);
        this.testCaseFilenameFilter = requireNonNull(testCaseFilenameFilter);
        this.fileFormat = requireNonNull(fileFormat);
        this.testCase = requireNonNull(testCase);
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
        // TODO make regenerate work on a system property

        System.getenv().forEach((k, v) -> System.out.println(k + " " + v));
        System.getProperties().forEach((k, v) -> System.out.println(k + " " + v));
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
                        new SnippetTest(
                                testCasesPath.resolve(filename),
                                regenerate,
                                fileFormat,
                                testCase)));
    }
}
