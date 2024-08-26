package com.backend.orderhere.dto.restaurant;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OpeningHourDTO {
  private Integer id;

  @Pattern(regexp = "^(?i)(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$", message = "dayOfWeek must be a valid day of the week")
  private String dayOfWeek;

  @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "openingTime must be in HH:mm format")
  private String openingTime;

  @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "closingTime must be in HH:mm format")
  private String closingTime;
}

