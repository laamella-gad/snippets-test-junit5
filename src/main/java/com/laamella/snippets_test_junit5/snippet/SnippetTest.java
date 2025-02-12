package com.laamella.snippets_test_junit5.snippet;

import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Path;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A single snippet unit test.
 */
class SnippetTest<T> implements Executable {
    private final Path testCaseFile;
    private final TestCaseProcessor<T> testCaseProcessor;
    private final ActualGenerator<T>[] actualGenerators;
    private final boolean regenerate;
    private final SnippetFileFormat fileFormat;

    @SafeVarargs
    protected SnippetTest(
            Path testCaseFile,
            boolean regenerate,
            SnippetFileFormat fileFormat,
            TestCaseProcessor<T> testCaseProcessor,
            ActualGenerator<T>... actualGenerators) {
        this.testCaseFile = requireNonNull(testCaseFile);
        this.regenerate = regenerate;
        this.fileFormat = requireNonNull(fileFormat);
        this.testCaseProcessor = requireNonNull(testCaseProcessor);
        this.actualGenerators = actualGenerators;
    }

    @Override
    public void execute() throws IOException {
        SnippetTestFile testFile = new SnippetTestFile(testCaseFile, fileFormat);
        testFile.read();
        T testCase = testCaseProcessor.processTestCase(testFile.testCase);

        // Put actual together:
        String actual = stream(actualGenerators)
                .map(actualGenerator -> actualGenerator.generate(testFile.testCase, testCase))
                .collect(joining(fileFormat.separatorBetweenExpectations));

        if (testFile.expected == null || regenerate) {
            // Write expected to test case:
            testFile.expected = actual;
            testFile.write();
            fail("Generated new expection file.");
        } else {
            // Compare with expected:
            assertEquals(testFile.expected, actual);
        }
    }
}
