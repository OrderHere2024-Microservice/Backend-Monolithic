package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.user.UserProfileUpdateDTO;
import com.backend.orderhere.dto.user.UserGetDto;
import com.backend.orderhere.dto.user.UserSignUpResponseDTO;
import com.backend.orderhere.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  void updateUserFromUserProfileUpdateDTO(UserProfileUpdateDTO dto, @MappingTarget User user);

  UserProfileUpdateDTO userToUserProfileUpdateDTO(User updatedUser);

  UserSignUpResponseDTO userToUserSignUpResponseDTO(User createdUser);

  UserGetDto userToUserGetDto(User user);

}
