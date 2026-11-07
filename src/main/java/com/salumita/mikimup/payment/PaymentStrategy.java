package com.salumita.mikimup.payment;

import java.math.BigDecimal;

public interface PaymentStrategy {

    String getMethod();

    String getStatus();

    String getReference();

    boolean validate(BigDecimal amount);
}
