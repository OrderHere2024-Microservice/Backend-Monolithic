package com.backend.orderhere.dto.order;

import com.backend.orderhere.dto.OrderDishDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OrderGetDTO {

  private Integer orderId;
  private Integer restaurantId;
  private String userId;
  private List<OrderDishDTO> dishes;
  private String username;
  private String orderStatus;
  private String orderType;
  private Integer tableNumber;
  private String pickupTime;
  private String address;
  private BigDecimal totalPrice;
  private String note;
  private ZonedDateTime updatedTime;
  private String phone;
  private Integer numberOfPeople;

}
