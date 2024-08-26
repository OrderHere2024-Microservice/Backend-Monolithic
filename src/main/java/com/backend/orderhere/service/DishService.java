package com.backend.orderhere.service;

import com.backend.orderhere.dto.PagingDto;
import com.backend.orderhere.dto.dish.DishCreateDto;
import com.backend.orderhere.dto.dish.DishGetDto;
import com.backend.orderhere.dto.dish.DishUpdateDTO;
import com.backend.orderhere.exception.ResourceNotFoundException;
import com.backend.orderhere.mapper.DishMapper;
import com.backend.orderhere.model.Dish;
import com.backend.orderhere.repository.DishRepository;
import com.backend.orderhere.service.enums.DishSort;
import com.backend.orderhere.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DishService {
  private final DishRepository dishRepository;
  private final DishMapper dishMapper;

  @Autowired
  private MinioService minioService;

  public PagingDto<List<DishGetDto>> getDishPageByRestaurantId(Integer restaurantId,
                                                               int page,
                                                               int size,
                                                               DishSort sort,
                                                               Sort.Direction order) {
    Pageable pageable = PageableUtil.determinePageable(page, size, Sort.by(order, sort.getName()));

    Page<Dish> dishPage = dishRepository.findDishesByRestaurantId(restaurantId, pageable);

    List<DishGetDto> dishGetDtoList = dishPage.getContent()
            .stream()
            .map(dishMapper::dishToDishGetDto)
            .toList();

    return PagingDto.<List<DishGetDto>>builder()
            .data(dishGetDtoList)
            .currentPage(dishPage.getNumber() + 1)
            .totalPages(dishPage.getTotalPages())
            .totalItems(dishPage.getTotalElements())
            .build();
  }

  public List<DishGetDto> getDishByCategory(Integer restaurantId, Integer categoryId) {
    return dishRepository.findAllByRestaurantIdAndCategoryCategoryId(restaurantId, categoryId)
            .stream()
            .map(dishMapper::dishToDishGetDto)
            .toList();
  }

  @Transactional
  public void createDish(DishCreateDto dishCreateDto) {
    try {
      String bucketName = "my-bucket";
      minioService.createBucket(bucketName);

      if (dishCreateDto.getImageFile() != null && !dishCreateDto.getImageFile().isEmpty()) {
        String imageUrl = minioService.uploadFile(dishCreateDto.getImageFile(), bucketName);
        dishCreateDto.setImageUrl(imageUrl);
      }

      Dish dish = dishMapper.dishCreateDtoToDish(dishCreateDto);
      dishRepository.save(dish);

    } catch (Exception e) {
      log.error("Error occurred while creating dish", e);
    }
  }

  @Transactional
  public DishGetDto updateDish(DishUpdateDTO dishUpdateDto) {
    try {
      Dish existingDish = dishRepository.findById(dishUpdateDto.getDishId())
              .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

      String bucketName = "my-bucket";
      minioService.createBucket(bucketName);

      if (dishUpdateDto.getImageFile() != null && !dishUpdateDto.getImageFile().isEmpty()) {
        String imageUrl = minioService.uploadFile(dishUpdateDto.getImageFile(), bucketName);
        dishUpdateDto.setImageUrl(imageUrl);
      } else {
        dishUpdateDto.setImageUrl(existingDish.getImageUrl());
      }

      dishMapper.updateDishFromDishUpdateDTO(dishUpdateDto, existingDish);
      dishRepository.save(existingDish);

      return dishMapper.dishToDishGetDto(existingDish);

    } catch (Exception e) {
      log.error("Error occurred while updating dish", e);
    }
    return null;
  }

  @Transactional
  public void deleteDish(Integer dishId) {
    Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + dishId));
    dishRepository.delete(dish);
  }
}
