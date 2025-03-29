package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.core.BasePath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;

import static com.laamella.snippets_test_junit5.snipsert.Snipsertions.snipsert;

class SnipsertExtensionTest {
    private static final BasePath basePath = BasePath.fromMavenModuleRoot(SnipsertExtensionTest.class).inSrcTestResources();

    @RegisterExtension
    static SnipsertExtension snipsertExtension = createSnipsertExtension(SnipsertExtensionTest.class);

    private static SnipsertExtension createSnipsertExtension(Class<?> testClass) {
        return new SnipsertExtension(
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