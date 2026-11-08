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
public class Product {

    private final int id;
    private final String name;
    private final BigDecimal price;
    private final String category;
    private final String description;

    public Product(
            int id,
            String name,
            BigDecimal price,
            String category,
            String description) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Harga tidak valid");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " - Rp " + price.toPlainString();
    }
}
