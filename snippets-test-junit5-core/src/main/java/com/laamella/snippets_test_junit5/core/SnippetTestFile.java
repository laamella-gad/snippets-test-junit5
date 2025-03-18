package com.laamella.snippets_test_junit5.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.laamella.snippets_test_junit5.core.Split.split;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.StandardOpenOption.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Responsible for the separation of the description, the test case, and the expectations in the snippet file.
 */
class SnippetTestFile {
    private final Path file;
    private final String separatorAfterTestCase;
    private final String expectationsEndString;
    private final String separatorBetweenTestCases;
    private final String descriptionStartString;
    private final String descriptionEndString;
    String description = null;
    List<String> testCaseParts = null;
    String expected = null;

    public SnippetTestFile(Path file, SnippetFileFormat fileFormat) {
        this.file = file;
        this.descriptionStartString = fileFormat.descriptionStartString;
        this.descriptionEndString = fileFormat.descriptionEndString;
        this.separatorBetweenTestCases = fileFormat.separatorBetweenTestCases;
        this.separatorAfterTestCase = fileFormat.separatorAfterTestCase;
        this.expectationsEndString = fileFormat.expectationsEndString;
    }

    void read() throws IOException {
        String completeFile = new String(readAllBytes(file), UTF_8);
        // Deal with description:
        final String fileAfterDescription;
        if (completeFile.startsWith(descriptionStartString)) {
            int descriptionEndPosition = completeFile.indexOf(descriptionEndString);
            if (descriptionEndPosition == -1) {
                fail("Starting description has no ending.");
            }
            description = completeFile.substring(0, descriptionEndPosition);
            fileAfterDescription = completeFile.substring(descriptionEndPosition + descriptionEndString.length());
        } else {
            fileAfterDescription = completeFile;
        }

        // Deal with test case and expectations:
        String testCase;
        int expectationsPosition = fileAfterDescription.indexOf(separatorAfterTestCase);
        if (expectationsPosition == -1) {
            testCase = fileAfterDescription;
        } else {
            testCase = fileAfterDescription.substring(0, expectationsPosition);
            expected = fileAfterDescription.substring(expectationsPosition + separatorAfterTestCase.length(), fileAfterDescription.length() - expectationsEndString.length());
        }
        testCaseParts = split(testCase, separatorBetweenTestCases);
    }

    void write() throws IOException {
        String content = String.join(separatorBetweenTestCases, testCaseParts) + separatorAfterTestCase + expected + expectationsEndString;
        if (description != null) {
            content = description + descriptionEndString + content;
        }
        Files.write(file, content.getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
    }
}
