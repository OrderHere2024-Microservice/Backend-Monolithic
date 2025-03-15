package com.backend.orderhere.service.handler;

import com.backend.orderhere.eventDto.PaymentSuccessEvent;
import com.backend.orderhere.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "payment-success-topic")
    public void handlePaymentSuccessEvent(String paymentSuccessEventMessage) throws JsonProcessingException {
        log.info("Payment Success Event received: {}", paymentSuccessEventMessage);
        PaymentSuccessEvent paymentSuccessEvent = objectMapper.readValue(paymentSuccessEventMessage, PaymentSuccessEvent.class);
        orderService.markOrderStatusAsPreparing(Integer.valueOf(paymentSuccessEvent.getOrderId()));
        log.info("Payment Success Event processed: {}", paymentSuccessEvent);
    }
}
