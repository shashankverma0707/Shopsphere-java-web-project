package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Reads database credentials from environment variables instead of source code. */
public final class DbConnection {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    private DbConnection() {}

    public static Connection get() throws SQLException {
        loadDriver();
        String url = env("ECOM_DB_URL", "jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC");
        String user = env("ECOM_DB_USER", "root");
        String password = env("ECOM_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

    private static void loadDriver() throws SQLException {
        try {
            Class.forName(MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver is missing from the application classpath.", e);
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
