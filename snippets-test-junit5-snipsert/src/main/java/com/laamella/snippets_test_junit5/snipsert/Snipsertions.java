package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.core.SnippetTestFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.StandardOpenOption.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class Snipsertions {
    static class AssertSnippet extends RuntimeException {
        final Object actual;

        AssertSnippet(Object actual) {
            this.actual = actual;
        }
    }

    /**
     * Asserts that a file with the name of the test contains actual. Writes actual to that file if it didn't exist.
     *
     * This assertions requires {@link SnipsertExtension}.
     *
     * The implementation is awkward and will generate strange stack traces when an assertion fails.
     */
    public static void snipsert(Object actual) {
        throw new AssertSnippet(actual);
    }

    /**
     * Asserts that filename contains actual. Writes actual to that file if it didn't exist.
     *
     * This is the most basic assertions.
     */
    public static void snipsert(String actual, String filename) {
        try {
            Path path = Paths.get(filename);
            Path directory = path.getParent();
            if (!Files.exists(directory)) {
                createDirectories(directory);
                fail("Created new testcases directory in " + path);
            }
            if (!Files.exists(path) || SnippetTestFactory.shouldRegenerate()) {
                Files.write(path, actual.getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
                fail("Generated new expectation file: " + path);
            }

            String completeFile = new String(readAllBytes(path), UTF_8);
            assertEquals(completeFile, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
