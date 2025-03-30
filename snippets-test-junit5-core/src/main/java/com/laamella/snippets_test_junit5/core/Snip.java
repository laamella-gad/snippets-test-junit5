package com.laamella.snippets_test_junit5.core;

import java.util.function.Consumer;

/**
 * A Snip can be injected into a test as a parameter by the {@link SnippetTestExtension}. Call "sert" on it to do the assert.
 *
 * Like Snipsertions.snipsert(actual), but it is well implemented.
 */
public class Snip {
    private final Consumer<Object> assertFileOnDisk;

    Snip(Consumer<Object> assertFileOnDisk) {
        this.assertFileOnDisk = assertFileOnDisk;
    }

    public void sert(String actual) {
        assertFileOnDisk.accept(actual);
    }
}
