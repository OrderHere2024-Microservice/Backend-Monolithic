package com.backend.orderhere.service;

import com.backend.orderhere.dto.rating.RatingGetDto;
import com.backend.orderhere.dto.rating.RatingPostDto;
import com.backend.orderhere.exception.ResourceNotFoundException;
import com.backend.orderhere.mapper.RatingMapper;
import com.backend.orderhere.model.Dish;
import com.backend.orderhere.model.Rating;
import com.backend.orderhere.repository.DishRepository;
import com.backend.orderhere.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final DishRepository dishRepository;

    public List<RatingGetDto> getRatingsByDishId(Integer dishId) {
        List<Rating> ratingList = ratingRepository.findAllByDishDishId(dishId);
        return ratingList.stream()
                .map(ratingMapper::ratingToRatingGetDto)
                .toList();
    }

    public List<RatingGetDto> getRatingsByUserId(String userId) {
        List<Rating> ratingList = ratingRepository.findAllByUserId(userId);
        return ratingList.stream().map(ratingMapper::ratingToRatingGetDto).toList();
    }

    public RatingGetDto createRating(RatingPostDto ratingPostDto) {
        Rating rating = ratingMapper.ratingPostDtoToRating(ratingPostDto);
        rating.setDish(dishRepository.findByDishIdAndIsDeletedFalse(ratingPostDto.getDishId()));
        ratingRepository.save(rating);
        updateDishRating(rating.getDish().getDishId());

        return ratingMapper.ratingToRatingGetDto(rating);
    }

    public void deleteRating(Integer ratingId) {
        ratingRepository.deleteByRatingId(ratingId);
        updateDishRating(ratingRepository.findByRatingId(ratingId).getDish().getDishId());
    }

    void updateDishRating(Integer dishId) {
        Long ratingCount = ratingRepository.countByDishId(dishId);
        BigDecimal averageRating = ratingRepository.calculateAverageRatingForDish(dishId);
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        if (averageRating == null ) {
            dish.setRating(null);
            log.info("Dish : {} doesn't have enough ratings yet", dishId);
        } else {
            dish.setRating(averageRating);
            log.info("Dish : {} current rating changes to {}", dishId, averageRating);
        }
        dishRepository.save(dish);
    }


    public List<RatingGetDto> createRatings(List<RatingPostDto> ratingPostDtos) {
        List<RatingGetDto> ratingGetDtos = new ArrayList<>();
        for (RatingPostDto ratingPostDto : ratingPostDtos) {

            Dish dish = dishRepository.findById(ratingPostDto.getDishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + ratingPostDto.getDishId()));
            Rating rating = new Rating();
            rating.setDish(dish);
            rating.setRatingValue(ratingPostDto.getRatingValue());
            rating.setComments(ratingPostDto.getComments());

            Rating savedRating = ratingRepository.save(rating);

            RatingGetDto ratingGetDto = ratingMapper.ratingToRatingGetDto(savedRating);
            ratingGetDtos.add(ratingGetDto);

            updateDishRating(dish.getDishId());
        }
        return ratingGetDtos;
    }
}
