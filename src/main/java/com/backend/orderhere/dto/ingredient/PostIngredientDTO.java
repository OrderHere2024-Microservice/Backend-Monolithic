package com.backend.orderhere.dto.ingredient;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PostIngredientDTO {
    private Integer dishId;
    private String name;
    private String unit;
    private BigDecimal quantityValue;
}
