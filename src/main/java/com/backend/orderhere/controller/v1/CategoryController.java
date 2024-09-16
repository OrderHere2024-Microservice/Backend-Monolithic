package com.backend.orderhere.controller.v1;

import com.backend.orderhere.dto.category.CategoryGetDto;
import com.backend.orderhere.dto.category.CategoryPostDto;
import com.backend.orderhere.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/public/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
  private final CategoryService categoryService;

  @Deprecated
  @GetMapping("/{restaurantId}")
  @ResponseStatus(HttpStatus.OK)
  public List<CategoryGetDto> getCategories(@PathVariable Integer restaurantId) {
    return categoryService.getCategoryByRestaurantId(restaurantId);
  }

  @Deprecated
  @PreAuthorize("hasRole('sys_admin')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryGetDto createCategory(@Valid @RequestBody CategoryPostDto categoryPostDto) {
    return categoryService.createCategory(categoryPostDto);
  }
}
