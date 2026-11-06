/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Salumita
 */
public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() {

        String productTable = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price TEXT NOT NULL,
                category TEXT NOT NULL,
                description TEXT
            )
            """;

        String transactionTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                order_source TEXT NOT NULL,
                created_at TEXT NOT NULL,
                total_price TEXT NOT NULL,
                status TEXT NOT NULL
            )
            """;

        String detailTable = """
            CREATE TABLE IF NOT EXISTS transaction_details (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                transaction_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                product_name TEXT NOT NULL,
                quantity INTEGER NOT NULL,
                unit_price TEXT NOT NULL,
                subtotal TEXT NOT NULL,
                FOREIGN KEY (transaction_id)
                    REFERENCES transactions(id),
                FOREIGN KEY (product_id)
                    REFERENCES products(id)
            )
            """;

        String paymentTable = """
            CREATE TABLE IF NOT EXISTS payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                transaction_id INTEGER NOT NULL,
                method TEXT NOT NULL,
                amount TEXT NOT NULL,
                status TEXT NOT NULL,
                reference TEXT,
                FOREIGN KEY (transaction_id)
                    REFERENCES transactions(id)
            )
            """;

        try (Connection connection
                = DatabaseConnection.getConnection(); Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");

            statement.execute(productTable);
            statement.execute(transactionTable);
            statement.execute(detailTable);
            statement.execute(paymentTable);

            System.out.println("Database Mikimup berhasil diinisialisasi.");

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal menginisialisasi database.", e
            );
        }
    }
}
