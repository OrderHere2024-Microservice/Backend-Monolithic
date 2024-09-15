package com.backend.orderhere.controller.v1;

import com.backend.orderhere.dto.restaurant.RestaurantCreateDTO;
import com.backend.orderhere.dto.restaurant.RestaurantGetDTO;
import com.backend.orderhere.dto.restaurant.RestaurantUpdateDTO;
import com.backend.orderhere.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/public/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;

  @Deprecated
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<RestaurantGetDTO> getAllRestaurants() {
    return restaurantService.getAllRestaurants();
  }

  @Deprecated
  @PreAuthorize("hasRole('sys_admin')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestaurantGetDTO createRestaurant(@Valid @RequestBody RestaurantCreateDTO restaurantCreateDTO) {
    return restaurantService.createRestaurant(restaurantCreateDTO);
  }

  @Deprecated
  @GetMapping("/{restaurantId}")
  @ResponseStatus(HttpStatus.OK)
  public RestaurantGetDTO getRestaurantById(@PathVariable Integer restaurantId) {
    return restaurantService.getRestaurantById(restaurantId);
  }

  @Deprecated
  @PreAuthorize("hasRole('sys_admin')")
  @PutMapping("/{restaurantId}")
  @ResponseStatus(HttpStatus.OK)
  public RestaurantGetDTO updateRestaurantById(@PathVariable Integer restaurantId, @Valid @RequestBody RestaurantUpdateDTO restaurantUpdateDTO) {
    return restaurantService.updateRestaurantById(restaurantId, restaurantUpdateDTO);
  }

}
