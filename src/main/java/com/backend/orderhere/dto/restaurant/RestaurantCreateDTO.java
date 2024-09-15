package com.backend.orderhere.dto.restaurant;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantCreateDTO {

  private String name;
  private String description;
  private String address;
  private String contactNumber;

  @Pattern(regexp = "^\\d{11}$", message = "Field must be 11 digits")
  private String abn;

  private String ownerName;
  private String ownerMobile;
  private String ownerAddress;

  @Pattern(regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Field must be a valid email address")
  private String ownerEmail;

  private String ownerCrn;

  private List<OpeningHourDTO> openingHours;
}
