package com.backend.orderhere.dto.userAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressPostDto {
  private Integer userId;
  private String address;
  private Boolean isDefault;
}
