package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.userAddress.UserAddressGetDto;
import com.backend.orderhere.dto.userAddress.UserAddressPostDto;
import com.backend.orderhere.dto.userAddress.UserAddressPutDto;
import com.backend.orderhere.model.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAddressMapper {
  UserAddressGetDto UserAddressToUserAddressGetDto(UserAddress userAddress);

  UserAddress UserAddressPostDtoToUserAddress(UserAddressPostDto userAddressPostDto);
}
