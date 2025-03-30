package com.laamella.snippets_test_junit5.core;

import java.nio.file.Path;

/**
 * Selects snippet files based on filename.
 */
@FunctionalInterface
public interface TestCaseFilenameFilter {
    boolean isTestCase(Path path);

    /**
     * @return a filter that accepts all files.
     */
    static TestCaseFilenameFilter allFiles() {
        return path -> true;
    }

    /**
     * @return a filter that accepts all files with a certain file extension.
     */
    static TestCaseFilenameFilter filesWithExtension(String extension) {
        String extensionWithDot = extension.startsWith(".") ? extension : "." + extension;
        return path -> path.toString().endsWith(extensionWithDot);
    }
}
