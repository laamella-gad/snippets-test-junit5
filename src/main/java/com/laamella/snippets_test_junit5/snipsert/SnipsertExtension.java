package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.BasePath;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.StandardOpenOption.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SnipsertExtension implements BeforeAllCallback, TestExecutionExceptionHandler {
    private Path testCasesPath;

    private final BasePath basePath;
    private final String fileExtension;
    private final List<ActualGenerator<?>> actualGenerators;
    private final String separatorBetweenExpectations;
    private final PostProcessor<?> postProcessor;

    @SafeVarargs
    public <T> SnipsertExtension(
            BasePath basePath,
            String fileExtension,
            String separatorBetweenExpectations,
            ,
            PostProcessor<T> postProcessor,
            ActualGenerator<T>... actualGenerators) {
        this.basePath = basePath;
        this.fileExtension = fileExtension;
        this.separatorBetweenExpectations = separatorBetweenExpectations;
        this.postProcessor = postProcessor;
        if (actualGenerators.length == 0) {
            this.actualGenerators = singletonList(Object::toString);
        } else {
            this.actualGenerators = asList(actualGenerators);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws IOException {
        testCasesPath = basePath.toPath().toAbsolutePath();

        if (!Files.exists(testCasesPath)) {
            createDirectories(testCasesPath);
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable.getClass() != Snipsert.AssertSnippet.class) {
            throw throwable;
        }
        Object rawActual = ((Snipsert.AssertSnippet) throwable).actual;
        Object actual = postProcessor.process(rawActual);
        final String actualText = actualGenerators.stream()
                .map(ag -> ((ActualGenerator) ag).generate(actual))
                .collect(joining(separatorBetweenExpectations));

        Path path = testCasesPath.resolve(context.getRequiredTestMethod().getName() + "." + fileExtension);
        if (!Files.exists(path) || shouldRegenerate(context)) {
            Files.write(path, actualText.getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
            Path relativize = basePath.toPath().relativize(path);

            fail("Generated new expection file: " + relativize);
        }

        String completeFile = new String(readAllBytes(path), UTF_8);
        assertEquals(completeFile, actual);
    }

    public static boolean shouldRegenerate(ExtensionContext context) {
        return context.getRequiredTestMethod().isAnnotationPresent(RegenerateExpectations.class) ||
                context.getRequiredTestClass().isAnnotationPresent(RegenerateExpectations.class) ||
                System.getProperties().containsKey("REGENERATE_EXPECTATIONS");
    }
}
