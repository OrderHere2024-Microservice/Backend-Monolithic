package com.backend.orderhere.service;

import com.backend.orderhere.dto.userAddress.UserAddressGetDto;
import com.backend.orderhere.dto.userAddress.UserAddressPostDto;
import com.backend.orderhere.dto.userAddress.UserAddressPutDto;
import com.backend.orderhere.mapper.UserAddressMapper;
import com.backend.orderhere.model.UserAddress;
import com.backend.orderhere.repository.UserAddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAddressService {
  private final UserAddressRepository userAddressRepository;
  private final UserAddressMapper userAddressMapper;

  public UserAddressGetDto getUserAddressByUserAddressId(Integer userAddressId) {
    UserAddress userAddress = userAddressRepository.findByUserAddressId(userAddressId)
        .orElseThrow(() -> {
          log.error("User address {} not found", userAddressId);
          return new RuntimeException("Resource not found");
        });
    return userAddressMapper.UserAddressToUserAddressGetDto(userAddress);
  }

  public List<UserAddressGetDto> getUserAddressByUserId(Integer userId) {
    List<UserAddress> userAddresses = userAddressRepository.findAllByUserId(userId);
    return userAddresses.stream()
        .map(userAddressMapper::UserAddressToUserAddressGetDto)
        .toList();
  }

  @Transactional
  public void deleteByUserAddressId(Integer userAddressId) {
    userAddressRepository.deleteByUserAddressId(userAddressId);
  }

  @Transactional
  public UserAddressGetDto updateUserAddressByUserAddressId(Integer userAddressId, UserAddressPutDto userAddressPutDto) {
    UserAddress userAddress = userAddressRepository.findByUserAddressId(userAddressId)
        .orElseThrow(
            () -> {
              log.error("User address {} not found", userAddressId);
              return new RuntimeException("Resource not found");
            }
        );
    userAddress.setAddress(userAddressPutDto.getAddress());
    userAddress.setIsDefault(userAddressPutDto.getIsDefault());
    if (userAddress.getIsDefault()) {
      userAddressRepository.setAllIsDefaultToFalseForUser(userAddress.getUserId());
    }
    userAddressRepository.save(userAddress);
    ensureLatestUpdatedAddressIsDefaultForUser(userAddress.getUserId());
    return userAddressMapper.UserAddressToUserAddressGetDto(userAddress);
  }

  @Transactional
  public UserAddressGetDto createUserAddress(UserAddressPostDto userAddressPostDto) {
    if (userAddressPostDto.getIsDefault()) {
      userAddressRepository.setAllIsDefaultToFalseForUser(userAddressPostDto.getUserId());
    }
    UserAddress userAddress = userAddressRepository.save(userAddressMapper.UserAddressPostDtoToUserAddress(userAddressPostDto));
    ensureLatestUpdatedAddressIsDefaultForUser(userAddressPostDto.getUserId());
    return userAddressMapper.UserAddressToUserAddressGetDto(userAddress);
  }


  @Transactional
  public void ensureLatestUpdatedAddressIsDefaultForUser(Integer userId) {
    Long defaultAddressesCount = userAddressRepository.countDefaultAddressesForUser(userId);

    if (defaultAddressesCount == 0) {
      Optional<UserAddress> latestUpdatedAddressOpt = userAddressRepository.findLatestUpdatedAddressByUserId(userId);
      if (latestUpdatedAddressOpt.isPresent()) {
        UserAddress latestUpdatedAddress = latestUpdatedAddressOpt.get();
        latestUpdatedAddress.setIsDefault(true);
        userAddressRepository.save(latestUpdatedAddress);
      }
    }
  }
}
