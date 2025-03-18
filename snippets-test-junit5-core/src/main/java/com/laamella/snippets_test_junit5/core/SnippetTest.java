package com.laamella.snippets_test_junit5.core;

import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A single snippet unit test.
 */
class SnippetTest implements Executable {
    private final Path testCaseFile;
    private final TestCase testCase;
    private final boolean regenerate;
    private final SnippetFileFormat fileFormat;

    protected SnippetTest(
            Path testCaseFile,
            boolean regenerate,
            SnippetFileFormat fileFormat,
            TestCase testCase) {
        this.testCaseFile = requireNonNull(testCaseFile);
        this.regenerate = regenerate;
        this.fileFormat = requireNonNull(fileFormat);
        this.testCase = requireNonNull(testCase);
    }

    @Override
    public void execute() throws IOException {
        SnippetTestFile testFile = new SnippetTestFile(testCaseFile, fileFormat);
        testFile.read();
        String actual = String.join(
                fileFormat.separatorBetweenExpectations,
                testCase.run(testFile.testCaseParts));

        if (testFile.expected == null || regenerate) {
            // Write expected to test case:
            testFile.expected = actual;
            testFile.write();
            fail("No expectation found. Generated one.");
        } else {
            // Compare with expected:
            assertEquals(testFile.expected, actual);
        }
    }
}
