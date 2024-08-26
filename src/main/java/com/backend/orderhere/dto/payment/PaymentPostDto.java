package com.backend.orderhere.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPostDto {
    private Integer orderId;
    private BigDecimal amount;
    private String currency;
}
