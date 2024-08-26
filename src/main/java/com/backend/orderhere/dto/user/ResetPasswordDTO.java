package com.backend.orderhere.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
  private String email;
  private String code;
  private String newPassword;
}
