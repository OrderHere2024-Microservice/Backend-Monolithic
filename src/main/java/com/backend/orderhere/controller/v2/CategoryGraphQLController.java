package com.backend.orderhere.controller.v2;

import com.backend.orderhere.dto.category.CategoryGetDto;
import com.backend.orderhere.dto.category.CategoryPostDto;
import com.backend.orderhere.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryGraphQLController {

    private final CategoryService categoryService;

    @QueryMapping
    public List<CategoryGetDto> getCategories(@Argument Integer restaurantId) {
        return categoryService.getCategoryByRestaurantId(restaurantId);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public CategoryGetDto createCategory(@Argument CategoryPostDto categoryPostDto) {
        return categoryService.createCategory(categoryPostDto);
    }
}
