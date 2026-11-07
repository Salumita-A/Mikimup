/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.model;

import java.math.BigDecimal;

/**
 *
 * @author Salumita
 */
public class TransactionDetail {

    private final int id;
    private final int productId;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal subtotal;

    public TransactionDetail(
            int id,
            int productId,
            String productName,
            int quantity,
            BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari nol");
        }

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;

        this.subtotal = unitPrice.multiply(
                BigDecimal.valueOf(quantity));
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
