package com.laamella.snippets_test_junit5.snipsert;

/**
 * Takes the actual that was passed to Snipsert.snipsert, and outputs an "actual" which
 * will be compared with the "expected" in the snippet file.
 */
@FunctionalInterface
public interface ActualGenerator<T> {
    String generate(T actual);
}
