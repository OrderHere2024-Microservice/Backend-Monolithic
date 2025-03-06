package com.backend.orderhere.service.handler;

import com.backend.orderhere.eventDto.DishDeletedEvent;
import com.backend.orderhere.repository.OrderRepository;
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
public class DishDeletedListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "dish-deleted-topic")
    public void handleDishDeletedEvent(String dishDeletedEventMessage) throws JsonProcessingException {
        log.info("Dish Deleted Event received: {}", dishDeletedEventMessage);
        DishDeletedEvent dishDeletedEvent = objectMapper.readValue(dishDeletedEventMessage, DishDeletedEvent.class);
        orderService.markDishAsDeleted(dishDeletedEvent.getDishId());
        log.info("Dish Deleted Event processed: {}", dishDeletedEvent);
    }
}
