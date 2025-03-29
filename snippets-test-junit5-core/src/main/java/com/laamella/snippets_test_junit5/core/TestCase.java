package com.laamella.snippets_test_junit5.core;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Takes the testcase strings as found in the snippet, and produces the
 * "actuals" as output.
 */
@FunctionalInterface
public interface TestCase {
    List<String> run(List<String> testCaseParts) throws Exception;

    /**
     * Convenience method that create a test case that has one input and one output.
     */
    static TestCase simpleTestCase(ThrowingFunction<String, String> testCase) {
        return testCaseParts -> {
            if (testCaseParts.size() != 1) {
                fail("Simple test case should have only one input, got " + testCaseParts.size());
            }
            return singletonList(testCase.apply(testCaseParts.get(0)));
        };
    }

    /**
     * Convenience method that create a test case that has one input and several outputs.
     */
    static TestCase multiInputTestCase(ThrowingFunction<List<String>, String> testCase) {
        return testCaseParts -> singletonList(testCase.apply(testCaseParts));
    }

    /**
     * Convenience method that create a test case that has one input and several outputs.
     */
    static TestCase multiOutputTestCase(ThrowingFunction<String, List<String>> testCase) {
        return testCaseParts -> {
            if (testCaseParts.size() != 1) {
                fail("Simple test case should have only one input, got " + testCaseParts.size());
            }
            return testCase.apply(testCaseParts.get(0));
        };
    }
}
