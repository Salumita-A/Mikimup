package com.salumita.mikimup.payment;

import java.math.BigDecimal;
import java.util.UUID;

public class QrisPayment implements PaymentStrategy {

    @Override
    public String getMethod() {
        return "QRIS";
    }

    @Override
    public String getStatus() {
        return "PENDING";
    }

    @Override
    public String getReference() {
        return "QRIS-" + UUID.randomUUID()
                .toString().substring(0, 8);
    }

    @Override
    public boolean validate(BigDecimal amount) {
        return amount != null && amount.signum() > 0;
    }
}
