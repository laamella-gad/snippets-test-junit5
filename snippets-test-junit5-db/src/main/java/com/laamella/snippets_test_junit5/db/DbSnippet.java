package com.laamella.snippets_test_junit5.db;

import com.laamella.snippets_test_junit5.table.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbSnippet {
    /**
     * Executes a query using the supplied credentials. Formats it as a table. To be used as an "actual"  in test cases.
     */
    public static String select(JdbcCredentials credentials, String query) throws SQLException {
        try (Connection connection = credentials.login()) {
            return select(connection, query);
        }
    }

    /**
     * Executes a query using the supplied, existing, connection. Formats it as a table. To be used as an "actual"  in test cases.
     */
    public static String select(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                Table resultTable = createTable(metaData);
                addRows(resultSet, metaData, resultTable);
                return resultTable.toString();
            }
        }
    }

    private static void addRows(ResultSet resultSet, ResultSetMetaData metaData, Table resultTable) throws SQLException {
        while (resultSet.next()) {
            List<String> values = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                values.add(resultSet.getString(i));
            }
            resultTable.rows.add(new Table.Row(values));
        }
    }

    private static Table createTable(ResultSetMetaData metaData) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        return new Table(columnNames);
    }
}
