package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.core.BasePath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;
import java.util.List;

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
                new ActualsGenerator() {
                    @Override
                    public List<String> generate(Object actual) {
                        String testString = (String) actual;
                        return Arrays.asList(testString, testString.toUpperCase(), testString.toLowerCase());
                    }
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
}