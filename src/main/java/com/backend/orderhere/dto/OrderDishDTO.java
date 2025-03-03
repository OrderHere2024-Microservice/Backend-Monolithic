package com.backend.orderhere.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDishDTO {
  private Integer dishId;
  private String dishName;
  private Integer dishQuantity;
  private BigDecimal dishPrice;
  private Boolean isDeleted = false;
}
