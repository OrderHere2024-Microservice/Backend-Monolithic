package com.backend.orderhere.auth;

import com.backend.orderhere.exception.ResourceNotFoundException;
import com.backend.orderhere.model.User;
import com.backend.orderhere.model.enums.UserRole;
import com.backend.orderhere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ApplicationUserDaoImpl implements ApplicationUserDao {

  private final UserRepository userRepository;

  @Override
  public Optional<ApplicationUserDetails> fetchUserByUserEmail(String email) {

    //retrieve user details from database
    User user = userRepository.findByEmail(email).
        orElseThrow(() -> new ResourceNotFoundException("user not found"));


    //create new applicationUserDetail and
    ApplicationUserDetails applicationUserDetails = new ApplicationUserDetails(
        user.getEmail(),
        user.getPassword(),
        getGrantedAuthorities(user.getUserRole()),
        true,
        true,
        true,
        true,
        user.getUserId(),
        user.getAvatarUrl(),
        user.getUsername()
    );
    return Optional.of(applicationUserDetails);
  }

  private Set<? extends GrantedAuthority> getGrantedAuthorities(UserRole userRole) {
    return Set.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
  }
}