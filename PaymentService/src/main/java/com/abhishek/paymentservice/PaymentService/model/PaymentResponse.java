package com.abhishek.paymentservice.PaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private long orderId;
    private long amount;
    private PaymentMode paymentMode;
    private String paymentStatus;
    private long paymentId;
    private Instant paymentDate;

}
