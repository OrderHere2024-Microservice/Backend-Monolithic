package com.backend.orderhere.controller.v1;

import com.backend.orderhere.dto.user.UserProfileUpdateDTO;
import com.backend.orderhere.dto.user.*;
import com.backend.orderhere.service.TokenService;
import com.backend.orderhere.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/users")
public class UserController {

  private final UserService userService;
  private final TokenService tokenService;

  @Autowired
  public UserController(UserService userService, TokenService tokenService) {
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @PutMapping("/{userId}/profile")
  public ResponseEntity<UserProfileUpdateDTO> updateProfile(@PathVariable Integer userId, @RequestBody UserProfileUpdateDTO dto) {
    UserProfileUpdateDTO updatedUserProfile = userService.updateUserProfile(userId, dto);
    return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
  }

  @PostMapping("/signup")
  public ResponseEntity<UserSignUpResponseDTO> userSignUp(@RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {
    UserSignUpResponseDTO user = userService.createUser(userSignUpRequestDTO);
    return new ResponseEntity<UserSignUpResponseDTO>(user, HttpStatus.OK);
  }

  @GetMapping("/profile")
  public ResponseEntity<?> getUserProfile(@RequestHeader(name = "Authorization") String authorizationHeader) {
    try {
      return new ResponseEntity<>(userService.getUserProfile(authorizationHeader), HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(e.getCause(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/profile")
  public ResponseEntity<UserProfileUpdateDTO> updateUserProfileWithToken(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody UserProfileUpdateDTO dto) {
    UserProfileUpdateDTO updatedUserProfile = userService.updateUserProfileWithToken(authorizationHeader, dto);
    return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
  }

  @PutMapping(value = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> updateUserAvatar(@RequestHeader(name = "Authorization") String authorizationHeader, @Valid @ModelAttribute UserAvatarUpdateDto userAvatarUpdateDto) {
    try {
      return new ResponseEntity<>(userService.updateUserAvatar(authorizationHeader, userAvatarUpdateDto), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
