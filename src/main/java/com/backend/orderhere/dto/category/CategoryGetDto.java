package com.backend.orderhere.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGetDto {
  private Integer categoryId;
  private Integer restaurantId;
  private String categoryName;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;
}
