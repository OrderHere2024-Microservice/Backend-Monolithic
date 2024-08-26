package com.backend.orderhere.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserSignUpResponseDTO {
  private int userId;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private ZonedDateTime createdTime;
  private ZonedDateTime updatedTime;
}
