package com.backend.orderhere.service;

import com.backend.orderhere.dto.user.*;
import com.backend.orderhere.exception.DataIntegrityException;
import com.backend.orderhere.exception.ResourceNotFoundException;
import com.backend.orderhere.filter.JwtUtil;
import com.backend.orderhere.mapper.UserMapper;
import com.backend.orderhere.model.User;
import com.backend.orderhere.model.UserAddress;
import com.backend.orderhere.model.enums.UserRole;
import com.backend.orderhere.repository.UserAddressRepository;
import com.backend.orderhere.repository.UserRepository;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

  //Declare Constant value for initialize user
  private final static int INIT_REWARD_POINT = 0;
  private final static String INIT_AVATAR_URL = "SOME_DEFAULT_URL";


  private final UserRepository userRepository;

  private final UserAddressRepository userAddressRepository;
  private final UserMapper userMapper;
  private final TokenService tokenService;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  private final MinioService minioService;

  @Autowired
  public UserService(UserRepository userRepository, UserMapper userMapper, TokenService tokenService, MinioService minioService, UserAddressRepository userAddressRepository) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.tokenService = tokenService;
    this.minioService = minioService;
    this.userAddressRepository = userAddressRepository;
  }

  public UserProfileUpdateDTO updateUserProfile(Integer userId, UserProfileUpdateDTO dto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    try {
      userMapper.updateUserFromUserProfileUpdateDTO(dto, user);
      User updatedUser = userRepository.save(user);

      return userMapper.userToUserProfileUpdateDTO(updatedUser);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException("Username already exists");
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong");
    }
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("User not found"));
  }

  public boolean resetPassword(String email, String code, String newPassword) {

    //retrieve user details
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("User not found"));

    //Create BCryptPassword
    String hashedPassword = encoder.encode(newPassword);

    if (tokenService.isCodeValid(code)) {
      user.setPassword(hashedPassword);
      userRepository.save(user);
      return true; // reset success
    }
    return false; // reset fail
  }

  public UserSignUpResponseDTO createUser(UserSignUpRequestDTO userSignUpRequestDTO) {

    //Create BCryptPassword
    String hashedPassword = encoder.encode(userSignUpRequestDTO.getPassword());
    //create user
    User user = User.builder()
        .username(userSignUpRequestDTO.getUserName())
        .firstname(userSignUpRequestDTO.getFirstName())
        .lastname(userSignUpRequestDTO.getLastName())
        .email(userSignUpRequestDTO.getEmail())
        .password(hashedPassword)
        .point(INIT_REWARD_POINT)
        .avatarUrl(INIT_AVATAR_URL)
        .userRole(UserRole.customer)
        .build();
    //create user and map user to response form
    User createdUser = userRepository.save(user);
    return userMapper.userToUserSignUpResponseDTO(createdUser);
  }

  public String createUser(OauthProviderLoginSessionDTO sessionData, String openId, String provider) {

    //Create BCryptPassword by userEmail
    String hashedPassword = encoder.encode(sessionData.getUsername() + sessionData.getEmail());
    //create user
    if(provider.equals("google")){
      User user = User.builder()
          .username(sessionData.getUsername())
          .firstname(sessionData.getUsername())
          .lastname(" ")
          .email(sessionData.getEmail())
          .password(hashedPassword)
          .point(INIT_REWARD_POINT)
          .avatarUrl(sessionData.getAvatarUrl())
          .userRole(UserRole.customer)
          .googleOpenId(openId)
          .build();
      User createdUser = userRepository.save(user);
      return JwtUtil.generateToken(createdUser);
    }else {
      User user = User.builder()
          .username(sessionData.getUsername())
          .firstname(sessionData.getUsername())
          .lastname(" ")
          .email(sessionData.getEmail())
          .password(hashedPassword)
          .point(INIT_REWARD_POINT)
          .avatarUrl(sessionData.getAvatarUrl())
          .userRole(UserRole.customer)
          .facebookOpenId(openId)
          .build();
      User createdUser = userRepository.save(user);
      return JwtUtil.generateToken(createdUser);
    }
  }

  public User createAndReturnUser(UserSignUpRequestDTO userSignUpRequestDTO) {
    String hashedPassword = encoder.encode(userSignUpRequestDTO.getPassword());
    User user = User.builder()
            .username(userSignUpRequestDTO.getUserName())
            .firstname(userSignUpRequestDTO.getFirstName())
            .lastname(userSignUpRequestDTO.getLastName())
            .email(userSignUpRequestDTO.getEmail())
            .password(hashedPassword)
            .point(INIT_REWARD_POINT)
            .avatarUrl(INIT_AVATAR_URL)
            .userRole(UserRole.customer)
            .build();
    return userRepository.save(user);
  }

  public String checkUserOpenId(String openId, String provider) {
    if(provider.equals("google")){
      Optional<User> existUser = userRepository.findByGoogleOpenId(openId);
      if(existUser.isPresent()){
        return JwtUtil.generateToken(existUser.get());
      }
    }else if(provider.equals("facebook")){
      Optional<User> existUser = userRepository.findByFacebookOpenId(openId);
      if(existUser.isPresent()){
        return JwtUtil.generateToken(existUser.get());
      }
    }
    return null;
  }

  public UserGetDto getUserProfile(String token) {
    Integer userId = JwtUtil.getUserIdFromToken(token);
    User user = userRepository.findByUserId(userId).orElseThrow();
    UserGetDto userGetDto = userMapper.userToUserGetDto(user);
    userAddressRepository.findFirstByUserIdAndIsDefault(userId, true)
            .ifPresent(userAddress -> userGetDto.setAddress(userAddress.getAddress()));
    return userGetDto;
  }

  @Transactional
  public UserProfileUpdateDTO updateUserProfileWithToken(String token, UserProfileUpdateDTO dto) {
    User user = userRepository.findById(JwtUtil.getUserIdFromToken(token))
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    try {
      userMapper.updateUserFromUserProfileUpdateDTO(dto, user);
      User updatedUser = userRepository.save(user);

      UserAddress address = userAddressRepository.findFirstByUserIdAndIsDefault(updatedUser.getUserId(), true)
              .orElseGet(() -> {
                UserAddress newAddress = new UserAddress();
                newAddress.setIsDefault(true);
                return newAddress;
              });

      address.setUserId(updatedUser.getUserId());
      address.setAddress(dto.getAddress());

      userAddressRepository.save(address);

      return userMapper.userToUserProfileUpdateDTO(updatedUser);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException("Username already exists");
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong");
    }
  }

  @Transactional
  public String updateUserAvatar(String token, UserAvatarUpdateDto userAvatarUpdateDto) throws Exception {
    System.out.println("got here");
    User user = userRepository.findById(JwtUtil.getUserIdFromToken(token))
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    String bucketName = "my-bucket";
    minioService.createBucket(bucketName);
    String imageUrl = minioService.uploadFile(userAvatarUpdateDto.getImageFile(), bucketName);
    System.out.println(imageUrl);
    user.setAvatarUrl(imageUrl);
    User updatedUser = userRepository.save(user);
    return updatedUser.getAvatarUrl();
  }
}




