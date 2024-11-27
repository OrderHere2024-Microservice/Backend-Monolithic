package com.backend.orderhere.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpResponseDTO {
  private String userId;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
}
