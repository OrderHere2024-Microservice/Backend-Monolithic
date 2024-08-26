package com.backend.orderhere.repository;

import com.backend.orderhere.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
  Page<Dish> findDishesByRestaurantId(Integer restaurantId, Pageable pageable);

  List<Dish> findAllByRestaurantIdAndCategoryCategoryId(Integer restaurantId, Integer categoryId);

  Dish findByDishId(Integer dishId);

}
