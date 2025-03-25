package com.laamella.snippets_test_junit5.snipsert;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.StandardOpenOption.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SnipsertExtension implements BeforeAllCallback, TestExecutionExceptionHandler {
    private Path testCasesPath;

    private final BasePath basePath;
    private final String fileExtension;
    private final String separatorBetweenExpectations;
    private final ActualsGenerator actualsGenerator;

    public SnipsertExtension(
            BasePath basePath,
            String fileExtension,
            String separatorBetweenExpectations,
            ActualsGenerator actualsGenerator) {
        this.basePath = basePath;
        this.fileExtension = fileExtension;
        this.separatorBetweenExpectations = separatorBetweenExpectations;
        this.actualsGenerator = actualsGenerator;
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
        final String actuals = String.join(separatorBetweenExpectations, actualsGenerator.generate(rawActual));

        Path path = testCasesPath.resolve(context.getRequiredTestMethod().getName() + "." + fileExtension);
        if (!Files.exists(path) || shouldRegenerate(context)) {
            Files.write(path, actuals.getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
            Path relativize = basePath.toPath().relativize(path);

            fail("Generated new expection file: " + relativize);
        }

        String completeFile = new String(readAllBytes(path), UTF_8);
        assertEquals(completeFile, actuals);
    }

    public static boolean shouldRegenerate(ExtensionContext context) {
        return context.getRequiredTestMethod().isAnnotationPresent(RegenerateExpectations.class) ||
                context.getRequiredTestClass().isAnnotationPresent(RegenerateExpectations.class) ||
                SnippetTestFactory.shouldRegenerate();
    }
}
