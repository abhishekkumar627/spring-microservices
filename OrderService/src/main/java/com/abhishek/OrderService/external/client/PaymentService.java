package com.abhishek.OrderService.external.client;

import com.abhishek.OrderService.exception.CustomException;
import com.abhishek.OrderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallBack")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    @PostMapping("/doPayment")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default ResponseEntity<Long> fallBack(Exception e) {
        throw new CustomException("Payment Service is not available", "NOT_AVAILABLE",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
