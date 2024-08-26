package com.backend.orderhere.mapper;

import com.backend.orderhere.dto.ingredient.GetIngredientDTO;
import com.backend.orderhere.model.LinkIngredientDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LinkIngredientDishMapper {
    @Mapping(target = "dishId", source = "dish.dishId")
    @Mapping(target = "ingredientId", source = "ingredient.ingredientId")
    GetIngredientDTO toDto(LinkIngredientDish linkIngredientDish);

}
