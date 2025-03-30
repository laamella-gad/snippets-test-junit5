package com.laamella.snippets_test_junit5.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;

import static com.laamella.snippets_test_junit5.core.Snipsertions.snipsert;

class SnippetTestExtensionTest {
    private static final BasePath basePath = BasePath.fromMavenModuleRoot(SnippetTestExtensionTest.class).inSrcTestResources();

    @RegisterExtension
    static SnippetTestExtension snippetTestExtension = createSnipsertExtension(SnippetTestExtensionTest.class);

    private static SnippetTestExtension createSnipsertExtension(Class<?> testClass) {
        return new SnippetTestExtension(
                basePath.inClassPackageAndNameSubDirectory(testClass),
                "txt",
                "\n---\n",
                actual -> {
                    String testString = (String) actual;
                    return Arrays.asList(testString, testString.toUpperCase(), testString.toLowerCase());
                });
    }

    @Test
    void abc() {
        snipsert("Henkie");
    }

    @Test
    void def() {
        snipsert("Klaas");
    }

    @Test
    void ghi() {
        snipsert("Pietje");
    }

    @Test
    void jkl(Snip snip) {
        snip.sert("Pietje");
    }

    @Test
    void mno() {
        Snipsertions.snipsert("Pietje", "src/test/resources/cases/pietje.txt");
    }
}