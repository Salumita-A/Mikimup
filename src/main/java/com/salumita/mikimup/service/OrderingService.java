package com.salumita.mikimup.service;

import com.salumita.mikimup.model.*;
import com.salumita.mikimup.payment.PaymentStrategy;
import com.salumita.mikimup.repository.PaymentRepository;
import com.salumita.mikimup.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderingService {

    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    public OrderingService(
            TransactionRepository transactionRepository,
            PaymentRepository paymentRepository) {

        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
    }

    public int checkout(
            Cart cart,
            String orderSource,
            PaymentStrategy strategy) {

        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException(
                    "Keranjang masih kosong."
            );
        }

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Metode pembayaran wajib dipilih."
            );
        }

        List<TransactionDetail> details = new ArrayList<>();

        for (Cart.Item item : cart.getItems()) {

            Product product = item.product();

            details.add(new TransactionDetail(
                    0,
                    product.getId(),
                    product.getName(),
                    item.quantity(),
                    product.getPrice()
            ));
        }

        Transaction transaction = new Transaction(
                0,
                orderSource,
                LocalDateTime.now(),
                details,
                "CREATED"
        );

        if (!strategy.validate(transaction.getTotalPrice())) {
            throw new IllegalArgumentException(
                    "Pembayaran tidak valid."
            );
        }

        int transactionId
                = transactionRepository.save(transaction);

        Payment payment = new Payment(
                0,
                transactionId,
                strategy.getMethod(),
                transaction.getTotalPrice(),
                strategy.getStatus(),
                strategy.getReference()
        );

        paymentRepository.save(payment);

        cart.clear();

        return transactionId;
    }
}
