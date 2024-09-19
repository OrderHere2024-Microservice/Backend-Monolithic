package com.backend.orderhere.controller.v1;

import com.backend.orderhere.controller.v2.IngredientGraphQLController;
import com.backend.orderhere.dto.ingredient.DeleteIngredientDTO;
import com.backend.orderhere.dto.ingredient.GetIngredientDTO;
import com.backend.orderhere.dto.ingredient.PostIngredientDTO;
import com.backend.orderhere.dto.ingredient.UpdateIngredientDTO;
import com.backend.orderhere.model.Ingredient;
import com.backend.orderhere.model.LinkIngredientDish;
import com.backend.orderhere.service.IngredientService;
import com.backend.orderhere.service.LinkIngredientDishService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/ingredients")
@Deprecated
@ConditionalOnMissingBean(IngredientGraphQLController.class)
public class IngredientController {

    private final IngredientService ingredientService;
    private final LinkIngredientDishService linkIngredientDishService;

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @PostMapping
    public ResponseEntity<Integer> createLink(@RequestBody PostIngredientDTO postIngredientDTO) {
        Integer ingredientId = linkIngredientDishService.createLink(postIngredientDTO);
        return ResponseEntity.ok(ingredientId);
    }

    @GetMapping("dish/{dishID}")
    public ResponseEntity<List<GetIngredientDTO>> findByDishID(@PathVariable Integer dishID) {
        List<GetIngredientDTO> dtoList = linkIngredientDishService.findGetIngredientDTOByDishID(dishID);
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLink(@RequestBody DeleteIngredientDTO deleteIngredientDTO) {
        try {
            linkIngredientDishService.deleteById(deleteIngredientDTO);
            return ResponseEntity.ok("Link deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable Integer id) {
        return ingredientService.getIngredientById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<Ingredient> updateIngredient(@RequestBody UpdateIngredientDTO updateIngredientDTO) {
        try {
            Ingredient updatedIngredient = ingredientService.updateIngredientName(updateIngredientDTO);
            return ResponseEntity.ok(updatedIngredient);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
