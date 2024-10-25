package com.laamella.snippets_test_junit5.snipsert;

public interface PostProcessor<T> {
    T process(Object actual);
}
