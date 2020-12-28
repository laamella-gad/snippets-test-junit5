package com.laamella.snippets_test_junit5;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

class SnippetTestFactoryTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(SnippetTestFactoryTest.class).inSrcTestResources();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory<>(
                basePath,
                path -> true,
                tc -> tc,
                ">>>",
                "<<<\n",
                "\n^^^\n",
                "\n---\n",
                "\nvvv\n",
                (testCaseText, testCase) -> testCase.toUpperCase()
        ).stream("test1");
    }
}