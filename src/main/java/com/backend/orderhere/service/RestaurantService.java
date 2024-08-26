package com.backend.orderhere.service;

import com.backend.orderhere.dto.restaurant.RestaurantCreateDTO;
import com.backend.orderhere.dto.restaurant.RestaurantGetDTO;
import com.backend.orderhere.dto.restaurant.RestaurantUpdateDTO;
import com.backend.orderhere.exception.DataIntegrityViolationException;
import com.backend.orderhere.exception.ResourceNotFoundException;
import com.backend.orderhere.mapper.RestaurantMapper;
import com.backend.orderhere.model.OpeningHours;
import com.backend.orderhere.model.Restaurant;
import com.backend.orderhere.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

  public final RestaurantRepository restaurantRepository;

  public final RestaurantMapper restaurantMapper;

  @Transactional
  public RestaurantGetDTO createRestaurant(RestaurantCreateDTO restaurantCreateDTO) {
    try {
      Restaurant restaurant = restaurantMapper.fromRestaurantCreateDTOToRestaurant(restaurantCreateDTO);
      if (restaurant.getOpeningHours() != null) {
        for (OpeningHours hours : restaurant.getOpeningHours()) {
          hours.setRestaurant(restaurant);
        }
      }
      restaurant = restaurantRepository.save(restaurant);
      return restaurantMapper.fromRestaurantToRestaurantGetDTO(restaurant);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException(e.getMessage());
    }
  }

  public RestaurantGetDTO getRestaurantById(Integer id) {
    Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    return restaurantMapper.fromRestaurantToRestaurantGetDTO(restaurant);
  }

  public List<RestaurantGetDTO> getAllRestaurants() {
    return restaurantRepository.findAll().stream().map(restaurantMapper::fromRestaurantToRestaurantGetDTO).collect(Collectors.toList());
  }

  @Transactional
  public RestaurantGetDTO updateRestaurantById(Integer id, RestaurantUpdateDTO restaurantUpdateDTO) {
    Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    try {
      restaurantMapper.updateRestaurantFromDto(restaurantUpdateDTO, restaurant);
      restaurant.getOpeningHours().stream().map((openingHours -> {
        openingHours.setRestaurant(restaurant);
        return openingHours;
      })).toList();
      Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
      return restaurantMapper.fromRestaurantToRestaurantGetDTO(updatedRestaurant);

    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException(e.getMessage());
    }
  }
}
