package com.abhishek.paymentservice.PaymentService.service;

import com.abhishek.paymentservice.PaymentService.entity.TransactionDetails;
import com.abhishek.paymentservice.PaymentService.model.PaymentRequest;
import com.abhishek.paymentservice.PaymentService.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Payment Request received : {}", paymentRequest);
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentMode(paymentRequest.getPaymentMode().name()).paymentStatus("SUCCESS").paymentDate(Instant.now())
                .orderId(paymentRequest.getOrderId()).referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount()).build();
        transactionDetailsRepository.save(transactionDetails);
        log.info("Payment Transaction Request saved with ID : {}", transactionDetails.getId());
        return transactionDetails.getId();
    }
}
