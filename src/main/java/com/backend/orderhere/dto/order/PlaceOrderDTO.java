package com.backend.orderhere.dto.order;

import com.backend.orderhere.dto.OrderDishDTO;
import com.backend.orderhere.model.enums.OrderStatus;
import com.backend.orderhere.model.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class PlaceOrderDTO {
//    private Integer userId;
    private Integer restaurantId;
    private Integer tableNumber;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private BigDecimal discount;
    private List<OrderDishDTO> dishes;
    private Double totalPrice;
    private String note;
    private String Address;
    private String phone;
    private Integer numberOfPeople;
    private ZonedDateTime pickupTime;
}
