package com.backend.orderhere.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthProviderLoginSessionDTO {
  private String username;
  private String email;
  private String avatarUrl;
}
