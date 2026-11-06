/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.repository;

import com.salumita.mikimup.database.DatabaseConnection;
import com.salumita.mikimup.model.Product;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Salumita
 */
public class ProductRepository {

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT id, name, price, category, description
            FROM products
            ORDER BY id
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Product product = new Product(
                        result.getInt("id"),
                        result.getString("name"),
                        new BigDecimal(result.getString("price")),
                        result.getString("category"),
                        result.getString("description")
                );

                products.add(product);
            }

            return products;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal mengambil data produk.", e
            );
        }
    }

    public Product findById(int id) {
        String sql = """
            SELECT id, name, price, category, description
            FROM products
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                return new Product(
                        result.getInt("id"),
                        result.getString("name"),
                        new BigDecimal(result.getString("price")),
                        result.getString("category"),
                        result.getString("description")
                );
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal mencari produk.", e
            );
        }
    }
}
