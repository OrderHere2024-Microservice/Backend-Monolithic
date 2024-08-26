package com.backend.orderhere.dto.ingredient;

import lombok.Data;

@Data
public class DeleteIngredientDTO {
    private Integer dishId;
    private Integer ingredientId;
}
