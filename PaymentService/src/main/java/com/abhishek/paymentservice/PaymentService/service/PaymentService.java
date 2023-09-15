package com.abhishek.paymentservice.PaymentService.service;

import com.abhishek.paymentservice.PaymentService.model.PaymentRequest;
import com.abhishek.paymentservice.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsUsingOrderId(String orderId);
}
