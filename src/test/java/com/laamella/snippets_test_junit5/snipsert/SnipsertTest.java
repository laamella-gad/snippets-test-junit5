package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.BasePath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class SnipsertTest {
    private static final BasePath basePath = BasePath.fromMavenModuleRoot(SnipsertTest.class).inSrcTestResources();

    @RegisterExtension
    static SnipsertExtension snipsertExtension = new SnipsertExtension(
            basePath.inClassPackageAndNameSubDirectory(SnipsertTest.class),
            "txt",
            "\n---\n",
            ac -> ac);

    @Test
    void abc() {
        Snipsert.snipsert("Henkie");
    }

    @Test
    void def() {
        Snipsert.snipsert("Klaas");
    }

    @Test
    void ghi() {
        Snipsert.snipsert("Pietje");
    }
}