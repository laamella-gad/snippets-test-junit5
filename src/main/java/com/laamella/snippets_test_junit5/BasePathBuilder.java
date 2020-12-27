package com.laamella.snippets_test_junit5;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Build a path to some location in your project.
 */
public class BasePathBuilder {
    private final Path path;

    private BasePathBuilder(Path path) {
        this.path = path;
    }

    public static BasePathBuilder fromClassLoaderRoot(Class<?> c) {
        try {
            return new BasePathBuilder(Paths.get(c.getProtectionDomain().getCodeSource().getLocation().toURI()));
        } catch (URISyntaxException e) {
            throw new AssertionError();
        }
    }

    public static BasePathBuilder fromMavenModuleRoot(Class<?> c) {
        return new BasePathBuilder(fromClassLoaderRoot(c).path.resolve(Paths.get("..", "..")).normalize());
    }

    public BasePathBuilder inSrcTestResources() {
        return new BasePathBuilder(path.resolve(Paths.get("src", "test", "resources")));
    }

    public BasePathBuilder inSubDirectory(String subDirectory) {
        return new BasePathBuilder(path.resolve(Paths.get(subDirectory)));
    }

    public Path build() {
        return path;
    }
}
