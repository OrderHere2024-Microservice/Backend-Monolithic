package com.backend.orderhere.filter;

import lombok.Data;

@Data
public class AuthenticationRequest {
  private String email;
  private String password;

}