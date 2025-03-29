package com.laamella.snippets_test_junit5.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcCredentials {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public JdbcCredentials(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public java.sql.Connection login() {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get a connection to " + jdbcUrl);
        }
    }
}