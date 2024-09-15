package com.backend.orderhere.controller.v2;

import com.backend.orderhere.dto.restaurant.RestaurantCreateDTO;
import com.backend.orderhere.dto.restaurant.RestaurantGetDTO;
import com.backend.orderhere.dto.restaurant.RestaurantUpdateDTO;
import com.backend.orderhere.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RestaurantGraphQLController {

    private final RestaurantService restaurantService;

    @QueryMapping
    public List<RestaurantGetDTO> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @QueryMapping
    public RestaurantGetDTO getRestaurantById(@Argument Integer restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public RestaurantGetDTO createRestaurant(@Argument RestaurantCreateDTO restaurantCreateDTO) {
        return restaurantService.createRestaurant(restaurantCreateDTO);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public RestaurantGetDTO updateRestaurantById(@Argument Integer restaurantId, @Argument RestaurantUpdateDTO restaurantUpdateDTO) {
        return restaurantService.updateRestaurantById(restaurantId, restaurantUpdateDTO);
    }
}
