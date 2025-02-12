package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.BasePath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.laamella.snippets_test_junit5.snipsert.Snipsert.snipsert;

class SnipsertTest {
    private static final BasePath basePath = BasePath.fromMavenModuleRoot(SnipsertTest.class).inSrcTestResources();

    @RegisterExtension
    static SnipsertExtension snipsertExtension = createSnipsertExtension(SnipsertTest.class);

    private static SnipsertExtension createSnipsertExtension(Class<?> testClass) {
        return new SnipsertExtension(
                basePath.inClassPackageAndNameSubDirectory(testClass),
                "txt",
                "\n---\n",
                ac -> ac);
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
}