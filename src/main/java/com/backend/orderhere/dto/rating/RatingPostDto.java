package com.backend.orderhere.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingPostDto {
  private String userId;
  private Integer dishId;
  private BigDecimal ratingValue;
  private String comments;
}
