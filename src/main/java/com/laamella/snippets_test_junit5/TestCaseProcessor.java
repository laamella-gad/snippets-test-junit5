package com.laamella.snippets_test_junit5;

/**
 * Takes the testcase string as found in the snippet, and produces the
 * object you want to use as input in your ActualGenerators.
 *
 * @param <T>
 */
@FunctionalInterface
public interface TestCaseProcessor<T> {
    T processTestCase(String testCase);
}
