package com.laamella.snippets_test_junit5.core;

import java.util.List;

/**
 * Takes the actual that was passed to {@link Snipsertions#snipsert(Object)}, and outputs "actuals" which
 * will be compared with the "expecteds" in the snippet file.
 */
@FunctionalInterface
public interface ActualsGenerator {
    List<String> generate(Object actual);
}
