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
public class Payment {

    private final int id;
    private final int transactionId;
    private final String method;
    private final BigDecimal amount;
    private final String status;
    private final String reference;

    public Payment(
            int id,
            int transactionId,
            String method,
            BigDecimal amount,
            String status,
            String reference) {

        this.id = id;
        this.transactionId = transactionId;
        this.method = method;
        this.amount = amount;
        this.status = status;
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getMethod() {
        return method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }
}
