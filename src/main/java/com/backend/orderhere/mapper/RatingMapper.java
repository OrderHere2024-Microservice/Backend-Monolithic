package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.rating.RatingGetDto;
import com.backend.orderhere.dto.rating.RatingPostDto;
import com.backend.orderhere.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapper {
  Rating ratingPostDtoToRating(RatingPostDto ratingPostDto);

  @Mapping(source = "user.userId", target = "userId")
  @Mapping(source = "dish.dishId", target = "dishId")
  RatingGetDto ratingToRatingGetDto(Rating rating);

}
