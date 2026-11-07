package com.salumita.mikimup.payment;

import java.math.BigDecimal;
import java.util.UUID;

public class CashPayment implements PaymentStrategy {

    @Override
    public String getMethod() {
        return "CASH";
    }

    @Override
    public String getStatus() {
        return "PAID";
    }

    @Override
    public String getReference() {
        return "CASH-" + UUID.randomUUID()
                .toString().substring(0, 8);
    }

    @Override
    public boolean validate(BigDecimal amount) {
        return amount != null && amount.signum() > 0;
    }
}
