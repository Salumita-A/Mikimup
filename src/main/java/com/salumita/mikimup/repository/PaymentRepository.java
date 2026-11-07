package com.salumita.mikimup.repository;

import com.salumita.mikimup.database.DatabaseConnection;
import com.salumita.mikimup.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentRepository {

    public void save(Payment payment) {

        String sql = """
            INSERT INTO payments
            (transaction_id, method, amount, status, reference)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(
                    1, payment.getTransactionId()
            );

            statement.setString(
                    2, payment.getMethod()
            );

            statement.setString(
                    3, payment.getAmount().toPlainString()
            );

            statement.setString(
                    4, payment.getStatus()
            );

            statement.setString(
                    5, payment.getReference()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal menyimpan pembayaran.", e
            );
        }
    }
}
