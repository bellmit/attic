package com.loginbox.app.acceptance.framework.database;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TemporaryDatabaseRule implements TestRule {
    private final String adminUrl;
    private final String username;
    private final String password;
    private final String databaseName;

    public TemporaryDatabaseRule(String adminUrl, String username, String password, String databaseName) {
        this.adminUrl = adminUrl;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                create();
                try {
                    base.evaluate();
                } finally {
                    drop();
                }
            }
        };
    }

    private void create() throws SQLException {
        String createQuery = String.format("CREATE DATABASE \"%s\"", databaseName);
        runStatement(createQuery);
    }

    private void drop() throws SQLException {
        String dropQuery = String.format("DROP DATABASE \"%s\"", databaseName);
        runStatement(dropQuery);
    }

    private void runStatement(String statement) throws SQLException {
        try (Connection connection = openAdminConnection()) {
            runStatement(connection, statement);
        }
    }

    private void runStatement(Connection connection, String statement) throws SQLException {
        try (PreparedStatement s = connection.prepareStatement(statement)) {
            s.executeUpdate();
        }
    }

    private Connection openAdminConnection() throws SQLException {
        return DriverManager.getConnection(adminUrl, username, password);
    }
}
