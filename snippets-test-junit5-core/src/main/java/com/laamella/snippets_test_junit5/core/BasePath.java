package com.laamella.snippets_test_junit5.core;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Build a path to some location in your project.
 */
public class BasePath {
    private final Path path;

    private BasePath(Path path) {
        this.path = path;
    }

    public static BasePath fromClassLoaderRoot(Class<?> c) {
        try {
            return new BasePath(Paths.get(c.getProtectionDomain().getCodeSource().getLocation().toURI()));
        } catch (URISyntaxException e) {
            throw new AssertionError();
        }
    }

    public static BasePath fromMavenModuleRoot(Class<?> c) {
        return new BasePath(fromClassLoaderRoot(c).path.resolve(Paths.get("..", "..")).normalize());
    }

    public static BasePath fromPath(Path path) {
        return new BasePath(path);
    }

    public BasePath inSrcTestResources() {
        return new BasePath(path.resolve(Paths.get("src", "test", "resources")));
    }

    public BasePath inSubDirectory(String subDirectory) {
        return new BasePath(path.resolve(Paths.get(subDirectory)));
    }

    public Path toPath() {
        return path;
    }
}
