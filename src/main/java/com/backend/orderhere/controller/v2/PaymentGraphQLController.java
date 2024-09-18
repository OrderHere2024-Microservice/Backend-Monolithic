package com.backend.orderhere.controller.v2;

import com.backend.orderhere.dto.payment.PaymentCreateDto;
import com.backend.orderhere.dto.payment.PaymentPostDto;
import com.backend.orderhere.dto.payment.PaymentResultDto;
import com.backend.orderhere.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentGraphQLController {

    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public PaymentCreateDto createPayment(@Argument PaymentPostDto paymentPostDto) {
        try {
            logger.info("Creating payment with paymentPostDto: {}, {}, {}", paymentPostDto.getAmount(), paymentPostDto.getCurrency(), paymentPostDto.getOrderId());
            return paymentService.createPayment(paymentPostDto);
        } catch (StripeException e) {
            log.error("Stripe error occurred: {}", e.getMessage());
            throw new RuntimeException("Failed to create payment: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public String getPaymentResult(@Argument PaymentResultDto paymentResultDto) {
        try {
            paymentService.getPaymentResult(paymentResultDto);
            return "Payment result processed successfully";
        } catch (StripeException e) {
            log.error("Stripe error occurred: {}", e.getMessage());
            throw new RuntimeException("Failed to process payment result: " + e.getMessage());
        }
    }
}
