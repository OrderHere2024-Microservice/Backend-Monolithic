package com.backend.orderhere.repository;

import com.backend.orderhere.model.Dish;
import com.backend.orderhere.model.Ingredient;
import com.backend.orderhere.model.LinkIngredientDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinkIngredientDishRepository extends JpaRepository<LinkIngredientDish, Integer> {
    List<LinkIngredientDish> findByDish(Dish dish);

    Optional<Object> findByDishAndIngredient(Dish dish, Ingredient ingredient);
}
