/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.model;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author Salumita
 */
public class Cart {

    public record Item(Product product, int quantity) {

        public BigDecimal subtotal() {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

    private final Map<Integer, Item> items
            = new LinkedHashMap<>();

    public void add(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Jumlah harus positif");
        }

        Item old = items.get(product.getId());

        items.put(
                product.getId(),
                new Item(
                        product,
                        quantity + (old == null ? 0 : old.quantity())));
    }

    public void remove(int productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public Collection<Item> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public BigDecimal total() {
        return items.values().stream()
                .map(Item::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
