package com.laamella.snippets_test_junit5.snipsert;

import java.util.function.Consumer;

/**
 * A Snip can be injected into a test as a parameter. Call "sert" on it to do the assert.
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
