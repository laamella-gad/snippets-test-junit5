package com.laamella.snippets_test_junit5.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Service {
    private final JdbcCredentials credentials;

    public Service(JdbcCredentials credentials) {
        this.credentials = credentials;
    }

    public void generateUsers(List<String> users) throws SQLException {
        try (Connection connection = credentials.login()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into users (name) values (?)")) {
                for (String user : users) {
                    statement.setString(1, user);
                    statement.executeUpdate();
                }
            }
        }
    }
}
