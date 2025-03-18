package com.laamella.snippets_test_junit5.core;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;
import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.filesWithExtension;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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