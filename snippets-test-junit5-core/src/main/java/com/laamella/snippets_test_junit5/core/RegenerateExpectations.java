package com.laamella.snippets_test_junit5.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When using the {@link SnippetTestExtension}, add this annotation to a method or class to regenerate all the expectations.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RegenerateExpectations {
}
