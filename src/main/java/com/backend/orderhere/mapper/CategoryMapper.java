package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.category.CategoryGetDto;
import com.backend.orderhere.dto.category.CategoryPostDto;
import com.backend.orderhere.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
  CategoryGetDto CategoryToCategoryGetDto(Category category);

  Category CategoryPostDtoToCategory(CategoryPostDto categoryPostDto);
}
