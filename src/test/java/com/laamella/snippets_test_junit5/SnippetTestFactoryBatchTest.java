package com.laamella.snippets_test_junit5;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.TestCaseFilenameFilter.*;

class SnippetTestFactoryBatchTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(SnippetTestFactoryBatchTest.class).inSrcTestResources();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetBatchTestFactory<>(
                new ExpectationFileFormat("\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("test1"),
                allFiles(),
                tc -> tc,
                (testCaseText, testCase) -> testCase.toUpperCase()
        ).stream();
    }
}