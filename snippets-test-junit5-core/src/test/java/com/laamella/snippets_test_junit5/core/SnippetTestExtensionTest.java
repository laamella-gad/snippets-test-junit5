package com.laamella.snippets_test_junit5.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;

import static com.laamella.snippets_test_junit5.core.Snipsertions.snipsert;

/**
 * Tests various ways of asserting test output with the contents of a file.
 */
class SnippetTestExtensionTest {
    private static final BasePath basePath = BasePath.fromMavenModuleRoot(SnippetTestExtensionTest.class).inSrcTestResources();

    /**
     * This extension handles the magic for the various asserts.
     */
    @RegisterExtension
    static SnippetTestExtension snippetTestExtension = createSnipsertExtension(SnippetTestExtensionTest.class);

    /**
     * You don't need to create this factory method for every test class.
     * Put it in a utility class and reuse it.
     */
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

    /**
     * Recommended: the SnippetTestExtension configures the Snip object.
     * Snip.sert checks the test output (Pietje) with the contents of jkl.txt,
     * based on the test method name and the configured file extension.
     */
    @Test
    void jkl(Snip snip) {
        snip.sert("Pietje");
    }

    /**
     * Like jkl, but no parameter is required.
     * Experimental: this does some dirty trickery behind the scenes.
     */
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

    /**
     * A very straightforward, but verbose approach.
     * Does not require the extension.
     */
    @Test
    void mno() {
        Snipsertions.snipsert("Pietje", "src/test/resources/cases/pietje.txt");
    }
}