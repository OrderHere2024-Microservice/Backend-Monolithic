package com.backend.orderhere.dto.payment;

import lombok.*;

@Data
@Builder
public class PaymentResultDto {
    private Integer paymentId;
    private String result;
}
