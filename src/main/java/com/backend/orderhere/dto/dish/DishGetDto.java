package com.backend.orderhere.dto.dish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishGetDto {
  private Integer dishId;
  private String dishName;
  private String description;
  private BigDecimal price;
  private String imageUrl;
  private BigDecimal rating;
  private Integer restaurantId;
  private Boolean availability;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;
  private Integer categoryId;
  private String categoryName;
}
