package com.abhishek.paymentservice.PaymentService.controller;

import com.abhishek.paymentservice.PaymentService.model.PaymentRequest;
import com.abhishek.paymentservice.PaymentService.model.PaymentResponse;
import com.abhishek.paymentservice.PaymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/doPayment")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<Long>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsUsingOrderId(@PathVariable String orderId) {
        return new ResponseEntity<PaymentResponse>(paymentService.getPaymentDetailsUsingOrderId(orderId),
                HttpStatus.OK);
    }
}
