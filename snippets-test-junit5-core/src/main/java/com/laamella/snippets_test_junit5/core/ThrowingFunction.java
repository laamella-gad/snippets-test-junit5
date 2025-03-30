package com.laamella.snippets_test_junit5.core;

@FunctionalInterface
public interface ThrowingFunction<A, B> {
    B apply(A a) throws Exception;
}
