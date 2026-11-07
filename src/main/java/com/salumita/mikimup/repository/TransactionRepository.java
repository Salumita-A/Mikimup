/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.repository;

import com.salumita.mikimup.database.DatabaseConnection;
import com.salumita.mikimup.model.Transaction;
import com.salumita.mikimup.model.TransactionDetail;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Salumita
 */
public class TransactionRepository {

    public int save(Transaction transaction) {

        String transactionSql = """
            INSERT INTO transactions
            (order_source, created_at, total_price, status)
            VALUES (?, ?, ?, ?)
            """;

        String detailSql = """
            INSERT INTO transaction_details
            (transaction_id, product_id, product_name,
             quantity, unit_price, subtotal)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection
                = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int transactionId;

                try (PreparedStatement statement
                        = connection.prepareStatement(
                                transactionSql,
                                Statement.RETURN_GENERATED_KEYS
                        )) {

                            statement.setString(
                                    1, transaction.getOrderSource()
                            );

                            statement.setString(
                                    2, transaction.getCreatedAt().toString()
                            );

                            statement.setString(
                                    3, transaction.getTotalPrice().toPlainString()
                            );

                            statement.setString(
                                    4, transaction.getStatus()
                            );

                            statement.executeUpdate();

                            try (ResultSet keys
                                    = statement.getGeneratedKeys()) {

                                if (!keys.next()) {
                                    throw new SQLException(
                                            "ID transaksi gagal dibuat."
                                    );
                                }

                                transactionId = keys.getInt(1);
                            }
                        }

                        try (PreparedStatement statement
                                = connection.prepareStatement(detailSql)) {

                            for (TransactionDetail detail
                                    : transaction.getDetails()) {

                                statement.setInt(1, transactionId);
                                statement.setInt(2, detail.getProductId());
                                statement.setString(3, detail.getProductName());
                                statement.setInt(4, detail.getQuantity());

                                statement.setString(
                                        5, detail.getUnitPrice().toPlainString()
                                );

                                statement.setString(
                                        6, detail.getSubtotal().toPlainString()
                                );

                                statement.addBatch();
                            }

                            statement.executeBatch();
                        }

                        connection.commit();
                        return transactionId;

            } catch (SQLException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal menyimpan transaksi.", e
            );
        }
    }

    public List<Transaction> findAll() {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
            SELECT id, order_source, created_at, status
            FROM transactions
            ORDER BY id DESC
            """;

        try (Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                int id = result.getInt("id");

                List<TransactionDetail> details
                        = findDetails(connection, id);

                Transaction transaction = new Transaction(
                        id,
                        result.getString("order_source"),
                        LocalDateTime.parse(
                                result.getString("created_at")
                        ),
                        details,
                        result.getString("status")
                );

                transactions.add(transaction);
            }

            return transactions;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal mengambil riwayat transaksi.", e
            );
        }
    }

    private List<TransactionDetail> findDetails(
            Connection connection, int transactionId)
            throws SQLException {

        List<TransactionDetail> details = new ArrayList<>();

        String sql = """
            SELECT id, product_id, product_name,
                   quantity, unit_price
            FROM transaction_details
            WHERE transaction_id = ?
            """;

        try (PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, transactionId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    details.add(new TransactionDetail(
                            result.getInt("id"),
                            result.getInt("product_id"),
                            result.getString("product_name"),
                            result.getInt("quantity"),
                            new BigDecimal(
                                    result.getString("unit_price")
                            )
                    ));
                }
            }
        }

        return details;
    }
}
