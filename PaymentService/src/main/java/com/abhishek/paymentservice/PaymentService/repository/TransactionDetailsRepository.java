package com.abhishek.paymentservice.PaymentService.repository;

import com.abhishek.paymentservice.PaymentService.entity.TransactionDetails;
import com.abhishek.paymentservice.PaymentService.model.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

    TransactionDetails findByOrderId(Long orderId);
}
