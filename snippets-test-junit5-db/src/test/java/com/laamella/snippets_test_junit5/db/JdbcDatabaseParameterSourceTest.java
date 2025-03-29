package com.laamella.snippets_test_junit5.db;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.core.TestCase.simpleTestCase;
import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;
import static java.util.Arrays.asList;

public class JdbcDatabaseParameterSourceTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(JdbcDatabaseParameterSourceTest.class).inSrcTestResources();

    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    private static Service service;
    private static JdbcCredentials credentials;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        postgreSQLContainer.start();
        credentials = new JdbcCredentials(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        service = new Service(credentials);
        try (Connection connection = credentials.login()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE users(id serial primary key, name varchar)");
            }
        }
    }

    @AfterAll
    public static void stopDatabase() {
        postgreSQLContainer.stop();
    }

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory(
                new SnippetFileFormat(">>>", "<<<\n", "\n---\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("cases"),
                allFiles(),
                simpleTestCase(this::runTestCase)
        ).stream();
    }

    private String runTestCase(String input) throws SQLException {
        service.generateUsers(asList(input.split("\n")));
        return DbSnippet.select(credentials, "select * from users");
    }
}
