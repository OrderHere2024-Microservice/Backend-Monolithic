package com.backend.orderhere.dto.userAddress;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressPutDto {
  @NotBlank(message = "Address must be provided")
  private String address;

  @NotNull
  private Boolean isDefault;
}
