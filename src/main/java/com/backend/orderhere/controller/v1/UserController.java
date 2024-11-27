package com.backend.orderhere.controller.v1;

import com.backend.orderhere.auth.KeyCloakService;
import com.backend.orderhere.dto.user.UserProfileUpdateDTO;
import com.backend.orderhere.dto.user.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/users")
public class UserController {

  private final KeyCloakService keyCloakService;

  @Autowired
  public UserController(KeyCloakService keyCloakService) {
    this.keyCloakService = keyCloakService;
  }

  @PutMapping("/{userId}/profile")
  public ResponseEntity<UserProfileUpdateDTO> updateProfile(@PathVariable String userId, @RequestBody UserProfileUpdateDTO dto) {

    UserProfileUpdateDTO updatedUserProfile = keyCloakService.updateUserProfile(userId, dto);
    return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
  }

  @GetMapping("/profile")
  public ResponseEntity<?> getUserProfile(@RequestHeader(name = "Authorization") String authorizationHeader) {
    try {
      return new ResponseEntity<>(keyCloakService.getUserProfile(authorizationHeader), HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(e.getCause(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/profile")
  public ResponseEntity<UserProfileUpdateDTO> updateUserProfileWithToken(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody UserProfileUpdateDTO dto) {
    UserProfileUpdateDTO updatedUserProfile = keyCloakService.updateUserProfileWithToken(authorizationHeader, dto);
    return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
  }

  @PutMapping(value = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> updateUserAvatar(@RequestHeader(name = "Authorization") String authorizationHeader, @Valid @ModelAttribute UserAvatarUpdateDto userAvatarUpdateDto) {
    try {
      return new ResponseEntity<>(keyCloakService.updateUserAvatar(authorizationHeader, userAvatarUpdateDto), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
