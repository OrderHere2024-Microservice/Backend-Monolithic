package com.backend.orderhere.repository;

import com.backend.orderhere.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  Optional<Category> findByCategoryId(Integer categoryId);

  List<Category> findAllByRestaurantId(Integer restaurantId);

  boolean existsByCategoryName(String categoryName);
}
