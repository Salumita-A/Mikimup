package com.salumita.mikimup.model;

import java.math.BigDecimal;
import java.util.*;

public class Cart {

    public record Item(Product product, int quantity) {

        public BigDecimal subtotal() {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

    private final Map<Integer, Item> items = new LinkedHashMap<>();

    // Method asli Anda
    public void add(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Jumlah harus positif");
        }
        Item old = items.get(product.getId());
        items.put(
                product.getId(),
                new Item(product, quantity + (old == null ? 0 : old.quantity()))
        );
    }

    // TAMBAHKAN INI: Agar cocok dengan pemanggilan di MainFrame.java (cart.addProduct(product))
    public void addProduct(Product product) {
        add(product, 1); // Default menambah quantity 1 setiap kali tombol diklik
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

    // TAMBAHKAN INI: Alias untuk total() agar cocok jika ada yang memanggil getTotal()
    public BigDecimal getTotal() {
        return total();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
