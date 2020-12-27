package com.laamella.snippets_test_junit5;

/**
 * Takes a (preprocessed) test case, and outputs an "actual" which
 * will be compared with the "expected" in the snippet file.
 *
 * @param <T> the preprocessed test case type
 */
public interface ActualGenerator<T> {
    String generate(String testCaseText, T testCase);
}
