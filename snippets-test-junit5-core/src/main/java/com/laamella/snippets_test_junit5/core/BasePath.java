package com.laamella.snippets_test_junit5.core;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

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

    public BasePath inPackageSubDirectory(Class<?> classInPackage) {
        return new BasePath(path.resolve(classInPackage.getPackage().getName().replace(".", "/")));
    }

    public BasePath inClassNameSubDirectory(Class<?> classWithName) {
        return new BasePath(path.resolve(camelCaseToSnakeCase(classWithName.getSimpleName())));
    }

    public BasePath inClassPackageAndNameSubDirectory(Class<?> classWithName) {
        return inPackageSubDirectory(classWithName).inClassNameSubDirectory(classWithName);
    }

    private String camelCaseToSnakeCase(String string) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (isUpperCase(ch)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public Path toPath() {
        return path;
    }
}
