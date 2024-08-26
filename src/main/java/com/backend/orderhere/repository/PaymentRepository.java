package com.backend.orderhere.repository;

import com.backend.orderhere.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment getByPaymentId(Integer paymentId);
}
