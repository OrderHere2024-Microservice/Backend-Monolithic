package com.backend.orderhere.dto.payment;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class PaymentPostDto {
    private Integer orderId;
    private BigDecimal amount;
    private String currency;
}
