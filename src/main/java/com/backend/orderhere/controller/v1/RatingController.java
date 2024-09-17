package com.backend.orderhere.controller.v1;

import com.backend.orderhere.dto.rating.RatingGetDto;
import com.backend.orderhere.dto.rating.RatingPostDto;
import com.backend.orderhere.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This controller is currently not being used in the frontend

@RestController
@RequestMapping("/v1/public/rating")
@RequiredArgsConstructor
@Validated
public class RatingController {
  private final RatingService ratingService;

  @GetMapping("/{dishId}")
  @ResponseStatus(HttpStatus.OK)
  public List<RatingGetDto> getRatingsByDishId(@PathVariable Integer dishId) {
    return ratingService.getRatingsByDishId(dishId);
  }

  @GetMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<RatingGetDto> getRatingsByUserId(@PathVariable Integer userId) {
    return ratingService.getRatingsByUserId(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RatingGetDto createRating(@Valid @RequestBody RatingPostDto ratingPostDto) {
    return ratingService.createRating(ratingPostDto);
  }

  @DeleteMapping("/{ratingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRating(@PathVariable Integer ratingId) {
    ratingService.deleteRating(ratingId);
  }

  @PostMapping("/submit-ratings")
  @ResponseStatus(HttpStatus.CREATED)
  public List<RatingGetDto> createRatings(@RequestBody List<RatingPostDto> ratingPostDtos) {
    return ratingService.createRatings(ratingPostDtos);
  }
}
