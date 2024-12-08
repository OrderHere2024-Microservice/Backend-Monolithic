package com.backend.orderhere.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserGetDto {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Integer point;
    private String avatarUrl;
    private String address;
}
