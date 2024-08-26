package com.backend.orderhere.repository;

import com.backend.orderhere.model.Restaurant;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Optional<Restaurant> findById(@NotNull Integer restaurantId);
}
