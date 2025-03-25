package com.laamella.snippets_test_junit5.core;

import java.util.List;

/**
 * Takes the testcase strings as found in the snippet, and produces the
 * "actuals" as output.
 */
@FunctionalInterface
public interface TestCase {
    List<String> run(List<String> testCaseParts) throws Exception;
}
