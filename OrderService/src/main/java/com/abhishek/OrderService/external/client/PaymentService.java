package com.abhishek.OrderService.external.client;

import com.abhishek.OrderService.external.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    @PostMapping("/doPayment")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);
}
