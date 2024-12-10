package com.backend.orderhere.dto.userAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressGetDto {
  private Integer userAddressId;
  private String userId;
  private String address;
  private Boolean isDefault;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;
}
