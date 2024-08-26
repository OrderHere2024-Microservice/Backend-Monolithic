package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.restaurant.RestaurantCreateDTO;
import com.backend.orderhere.dto.restaurant.RestaurantGetDTO;
import com.backend.orderhere.dto.restaurant.RestaurantUpdateDTO;
import com.backend.orderhere.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

  @Mapping(target = "openingHours", source = "openingHours")
  RestaurantGetDTO fromRestaurantToRestaurantGetDTO(Restaurant restaurant);

  @Mapping(target = "openingHours", source = "openingHours")
  Restaurant fromRestaurantCreateDTOToRestaurant(RestaurantCreateDTO restaurantCreateDTO);

  void updateRestaurantFromDto(RestaurantUpdateDTO dto, @MappingTarget Restaurant restaurant);
}
