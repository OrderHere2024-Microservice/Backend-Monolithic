package com.backend.orderhere.controller.v1;

import com.backend.orderhere.dto.payment.PaymentCreateDto;
import com.backend.orderhere.dto.payment.PaymentPostDto;
import com.backend.orderhere.dto.payment.PaymentResultDto;
import com.backend.orderhere.service.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/public/pay")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @Deprecated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPaymentIntent(@RequestBody @Valid PaymentPostDto paymentPostDto) {
        System.out.println("called");
        try {
            PaymentCreateDto createdPayment = paymentService.createPayment(paymentPostDto);
            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
        } catch (StripeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Deprecated
    @PutMapping("/result")
    @ResponseStatus(HttpStatus.OK)
    public void getPaymentResult(@RequestBody @Valid PaymentResultDto paymentResultDto) {
        try {
            paymentService.getPaymentResult(paymentResultDto);
        } catch (StripeException e) {
            log.error(e.getMessage());
        }
    }
}
