package com.backend.orderhere.dto.payment;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class PaymentCreateDto {
    private Integer paymentId;
    private String clientSecret;
}
