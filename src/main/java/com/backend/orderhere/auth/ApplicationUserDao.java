package com.backend.orderhere.auth;

import java.util.Optional;

public interface ApplicationUserDao {
  Optional<ApplicationUserDetails> fetchUserByUserEmail(String email);
}