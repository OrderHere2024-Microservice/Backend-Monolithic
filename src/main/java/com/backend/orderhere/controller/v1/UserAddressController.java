package com.backend.orderhere.controller.v1;

import com.backend.orderhere.dto.userAddress.UserAddressGetDto;
import com.backend.orderhere.dto.userAddress.UserAddressPostDto;
import com.backend.orderhere.dto.userAddress.UserAddressPutDto;
import com.backend.orderhere.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This controller is currently not being used in the frontend
// @RestController
@RequestMapping("/v1/public/user-address")
@RequiredArgsConstructor
@Validated
public class UserAddressController {
  private final UserAddressService userAddressService;

  @GetMapping("/{userAddressId}")
  @ResponseStatus(HttpStatus.OK)
  public UserAddressGetDto getByUserAddressId(@PathVariable Integer userAddressId) {
    return userAddressService.getUserAddressByUserAddressId(userAddressId);
  }

  @GetMapping("/all/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<UserAddressGetDto> getByUserId(@PathVariable String userId) {
    return userAddressService.getUserAddressByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserAddressGetDto createUserAddress(@Valid @RequestBody UserAddressPostDto userAddressPostDto) {
    return userAddressService.createUserAddress(userAddressPostDto);
  }

  @DeleteMapping("/{userAddressId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteByUserAddressId(@PathVariable Integer userAddressId) {
    userAddressService.deleteByUserAddressId(userAddressId);
  }

  @PutMapping("/{userAddressId}")
  @ResponseStatus(HttpStatus.OK)
  public UserAddressGetDto updatedUserAddress(@PathVariable Integer userAddressId, @RequestBody UserAddressPutDto userAddressPutDto) {
    return userAddressService.updateUserAddressByUserAddressId(userAddressId, userAddressPutDto);
  }
}
