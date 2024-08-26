package com.backend.orderhere.repository;

import com.backend.orderhere.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
  @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.dish.dishId = :dishId")
  BigDecimal calculateAverageRatingForDish(@Param("dishId") Integer dishId);

  @Query("SELECT COUNT(r) FROM Rating r WHERE r.dish.dishId = :dishId")
  Long countByDishId(@Param("dishId") Integer dishId);

  Rating findByRatingId(Integer ratingId);

  List<Rating> findAllByDishDishId(Integer dishId);

  List<Rating> findAllByUserUserId(Integer userId);

  void deleteByRatingId(Integer ratingId);
}
