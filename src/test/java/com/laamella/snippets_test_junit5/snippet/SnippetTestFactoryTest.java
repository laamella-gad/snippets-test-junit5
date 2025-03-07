package com.laamella.snippets_test_junit5.snippet;

import com.laamella.snippets_test_junit5.BasePath;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.snippet.TestCaseFilenameFilter.*;

class SnippetTestFactoryTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(SnippetTestFactoryTest.class).inSrcTestResources();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory<>(
                new SnippetFileFormat(">>>", "<<<\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("test1"),
                allFiles(),
                tc -> tc,
                (testCaseText, testCase) -> testCase.toUpperCase()
        ).stream();
    }
}