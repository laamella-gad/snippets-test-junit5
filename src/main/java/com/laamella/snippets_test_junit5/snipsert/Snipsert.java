package com.laamella.snippets_test_junit5.snipsert;

public class Snipsert {
    static class AssertSnippet extends RuntimeException {
        final Object actual;

        AssertSnippet(Object actual) {
            this.actual = actual;
        }
    }

    public static void snipsert(Object actual) {
        throw new AssertSnippet(actual);
    }
}
