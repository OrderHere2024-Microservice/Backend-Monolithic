package com.backend.orderhere.dto.restaurant;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RestaurantGetDTO {

  private Integer restaurantId;
  private String name;
  private String description;
  private String address;
  private String contactNumber;
  private String abn;
  private String ownerName;
  private String ownerMobile;
  private String ownerAddress;
  private String ownerEmail;
  private String ownerCrn;
  private BigDecimal averageRating;

  private List<OpeningHourDTO> openingHours;

}
