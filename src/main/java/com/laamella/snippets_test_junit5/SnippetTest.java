package com.laamella.snippets_test_junit5;

import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

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
    private final String separatorBetweenExpectations;
    private final String expectationsEndString;
    private final Function<String, T> testCaseProcessor;
    private final ActualGenerator<T>[] actualGenerators;
    private final String separatorAfterTestCase;
    private final String descriptionStartString;
    private final String descriptionEndString;
    private final boolean regenerate;

    @SafeVarargs
    protected SnippetTest(
            Path testCaseFile,
            boolean regenerate, String descriptionStartString,
            String descriptionEndString,
            String separatorAfterTestCase,
            String separatorBetweenExpectations,
            String expectationsEndString,
            Function<String, T> testCaseProcessor,
            ActualGenerator<T>... actualGenerators) {
        this.testCaseFile = requireNonNull(testCaseFile);
        this.regenerate = regenerate;
        this.separatorAfterTestCase = requireNonNull(separatorAfterTestCase);
        this.separatorBetweenExpectations = requireNonNull(separatorBetweenExpectations);
        this.expectationsEndString = requireNonNull(expectationsEndString);
        this.testCaseProcessor = requireNonNull(testCaseProcessor);
        this.actualGenerators = actualGenerators;
        this.descriptionStartString = requireNonNull(descriptionStartString);
        this.descriptionEndString = requireNonNull(descriptionEndString);
    }

    @Override
    public void execute() throws IOException {
        SnippetTestFile testFile = new SnippetTestFile(testCaseFile, descriptionStartString, descriptionEndString, separatorAfterTestCase, expectationsEndString);
        testFile.read();
        T testCase = testCaseProcessor.apply(testFile.testCase);

        // Put actual together:
        String actual = stream(actualGenerators)
                .map(actualGenerator -> actualGenerator.generate(testFile.testCase, testCase))
                .collect(joining(separatorBetweenExpectations));

        if (testFile.expected == null || regenerate) {
            // Write expected to test case:
            testFile.expected = actual;
            testFile.write();
            fail("No expectation found. Generated one.");
        } else {
            // Compare with expected:
            assertEquals(testFile.expected.trim(), actual.trim());
        }
    }
}
