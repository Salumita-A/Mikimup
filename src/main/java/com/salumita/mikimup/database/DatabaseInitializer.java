package com.salumita.mikimup.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initialize() {

        String url = "jdbc:sqlite:mikimup.db";

        try (Connection connection
                = DriverManager.getConnection(url)) {

            // Mengaktifkan foreign key
            try (Statement statement = connection.createStatement()) {

                statement.execute("PRAGMA foreign_keys = ON");

                // Tabel produk
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS products (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        price TEXT NOT NULL,
                        category TEXT,
                        description TEXT
                    )
                """);

                // Tabel transaksi
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        order_source TEXT NOT NULL,
                        created_at TEXT NOT NULL,
                        total_price TEXT NOT NULL,
                        status TEXT NOT NULL
                    )
                """);

                // Tabel detail transaksi
                statement.execute("""
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
                """);

                // Tabel pembayaran
                statement.execute("""
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
                """);
            }

            // Memasukkan produk awal
            seedProducts(connection);

            System.out.println(
                    "Database Mikimup berhasil diinisialisasi!"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Gagal menginisialisasi database: "
                    + e.getMessage(), e
            );
        }
    }

    private static void seedProducts(Connection connection)
            throws SQLException {

        String checkSql
                = "SELECT COUNT(*) FROM products";

        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(checkSql)) {

            if (result.next() && result.getInt(1) > 0) {

                System.out.println(
                        "Data produk sudah tersedia."
                );

                return;
            }
        }

        String insertSql = """
            INSERT INTO products
            (name, price, category, description)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement statement
                = connection.prepareStatement(insertSql)) {

            addProduct(
                    statement,
                    "Original Kimbab",
                    "18000",
                    "Kimbab",
                    "Kimbab klasik dengan isian sayuran dan telur"
            );

            addProduct(
                    statement,
                    "Beef Kimbab",
                    "25000",
                    "Kimbab",
                    "Kimbab dengan isian daging sapi"
            );

            addProduct(
                    statement,
                    "Chicken Kimbab",
                    "22000",
                    "Kimbab",
                    "Kimbab dengan isian ayam"
            );

            addProduct(
                    statement,
                    "Tuna Kimbab",
                    "25000",
                    "Kimbab",
                    "Kimbab dengan isian tuna"
            );

            addProduct(
                    statement,
                    "Spicy Chicken Kimbab",
                    "25000",
                    "Kimbab",
                    "Kimbab ayam dengan saus pedas"
            );

            addProduct(
                    statement,
                    "Cheese Kimbab",
                    "23000",
                    "Kimbab",
                    "Kimbab dengan isian keju"
            );

            addProduct(
                    statement,
                    "Crab Stick Kimbab",
                    "22000",
                    "Kimbab",
                    "Kimbab dengan isian crab stick"
            );

            addProduct(
                    statement,
                    "Beef Bulgogi Kimbab",
                    "28000",
                    "Kimbab",
                    "Kimbab dengan daging sapi bulgogi"
            );

            statement.executeBatch();
        }

        System.out.println(
                "8 produk kimbab berhasil ditambahkan!"
        );
    }

    private static void addProduct(
            PreparedStatement statement,
            String name,
            String price,
            String category,
            String description
    ) throws SQLException {

        statement.setString(1, name);
        statement.setString(2, price);
        statement.setString(3, category);
        statement.setString(4, description);

        statement.addBatch();
    }
}
