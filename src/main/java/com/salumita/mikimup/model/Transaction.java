/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Salumita
 */
public class Transaction {

    private final int id;
    private final String orderSource;
    private final LocalDateTime createdAt;
    private final BigDecimal totalPrice;
    private final String status;
    private final List<TransactionDetail> details;

    public Transaction(
            int id,
            String orderSource,
            LocalDateTime createdAt,
            List<TransactionDetail> details,
            String status) {

        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException(
                    "Transaksi minimal memiliki satu detail");
        }

        this.id = id;
        this.orderSource = Objects.requireNonNull(orderSource);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.details = List.copyOf(details);
        this.status = status;

        this.totalPrice = details.stream()
                .map(TransactionDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getId() {
        return id;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public List<TransactionDetail> getDetails() {
        return details;
    }
}
