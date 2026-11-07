/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salumita.mikimup.payment;

import java.math.BigDecimal;

/**
 *
 * @author Salumita
 */
public class PaymentStrategy {

    String getMethod();

    String getStatus();

    String getReference();

    boolean validate(BigDecimal amount);
}
