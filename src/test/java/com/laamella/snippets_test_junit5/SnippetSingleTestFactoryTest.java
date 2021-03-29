package com.laamella.snippets_test_junit5;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

class SnippetSingleTestFactoryTest {
    @Test
    void abc() {
        ExpectationFileTester<String> tester = new ExpectationFileTester<>(
                new ExpectationFileFormat("BEGIN\n", "\nAND\n", "\nEND\n"),
                BasePath.fromMavenModuleRoot(SnippetSingleTestFactoryTest.class)
                        .inSrcTestResources()
                        .inSubDirectory("expectations"),
                ".txt",
                String::toUpperCase,
                (tc, s) -> Arrays.toString(s.getBytes(StandardCharsets.UTF_8))
        );
        tester.test("1+1");
    }
}