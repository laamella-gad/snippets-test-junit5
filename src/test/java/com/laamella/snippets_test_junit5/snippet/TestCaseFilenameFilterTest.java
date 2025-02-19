package com.laamella.snippets_test_junit5.snippet;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.laamella.snippets_test_junit5.snippet.TestCaseFilenameFilter.*;
import static org.junit.jupiter.api.Assertions.*;

class TestCaseFilenameFilterTest {
    @Test
    void testAllFiles() {
        TestCaseFilenameFilter filter = allFiles();
        assertTrue(filter.isTestCase(Paths.get("/a/b/c")));
        assertTrue(filter.isTestCase(Paths.get("c.txt")));
    }

    @Test
    void testExtensionWithDot() {
        TestCaseFilenameFilter koeFilter = filesWithExtension(".koe");
        assertTrue(koeFilter.isTestCase(Paths.get("/a/b/c.koe")));
        assertFalse(koeFilter.isTestCase(Paths.get("koe")));
    }

    @Test
    void testExtensionWithoutDot() {
        TestCaseFilenameFilter koeFilter = filesWithExtension("koe");
        assertTrue(koeFilter.isTestCase(Paths.get("/a/b/c.koe")));
        // a leading dot is enforced.
        assertFalse(koeFilter.isTestCase(Paths.get("koe")));
    }
}