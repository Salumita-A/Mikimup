package com.salumita.mikimup.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Salumita
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:mikimup.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
