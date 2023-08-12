package com.abhishek.paymentservice.PaymentService.service;

import com.abhishek.paymentservice.PaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
