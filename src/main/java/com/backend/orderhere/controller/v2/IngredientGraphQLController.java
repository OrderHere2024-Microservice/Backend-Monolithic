package com.backend.orderhere.controller.v2;

import com.backend.orderhere.dto.ingredient.DeleteIngredientDTO;
import com.backend.orderhere.dto.ingredient.GetIngredientDTO;
import com.backend.orderhere.dto.ingredient.PostIngredientDTO;
import com.backend.orderhere.dto.ingredient.UpdateIngredientDTO;
import com.backend.orderhere.model.Ingredient;
import com.backend.orderhere.service.IngredientService;
import com.backend.orderhere.service.LinkIngredientDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IngredientGraphQLController {

    private final IngredientService ingredientService;
    private final LinkIngredientDishService linkIngredientDishService;

    @QueryMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @QueryMapping
    public List<GetIngredientDTO> findIngredientsByDishID(@Argument Integer dishID) {
        return linkIngredientDishService.findGetIngredientDTOByDishID(dishID);
    }

    @QueryMapping
    public Ingredient getIngredientById(@Argument Integer id) {
        return ingredientService.getIngredientById(id);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public Integer createLinkIngredientDish(@Argument PostIngredientDTO postIngredientDTO) {
        return linkIngredientDishService.createLink(postIngredientDTO);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public Ingredient updateIngredient(@Argument UpdateIngredientDTO updateIngredientDTO) {
        return ingredientService.updateIngredientName(updateIngredientDTO);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public String deleteIngredientLink(@Argument DeleteIngredientDTO deleteIngredientDTO) {
        try {
            linkIngredientDishService.deleteById(deleteIngredientDTO);
            return "Link deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting ingredient link: " + e.getMessage());
        }
    }
}
