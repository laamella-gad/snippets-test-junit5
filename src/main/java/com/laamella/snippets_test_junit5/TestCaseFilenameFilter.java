package com.laamella.snippets_test_junit5;

import java.nio.file.Path;

@FunctionalInterface
public interface TestCaseFilenameFilter {
    boolean isTestCase(Path path);
}
