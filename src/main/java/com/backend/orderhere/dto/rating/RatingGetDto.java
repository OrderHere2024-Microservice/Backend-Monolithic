package com.backend.orderhere.dto.rating;

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
public class RatingGetDto {
  private Integer ratingId;
  private String userId;
  private Integer dishId;
  private BigDecimal ratingValue;
  private String comments;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;
}
