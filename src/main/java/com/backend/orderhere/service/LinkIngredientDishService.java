package com.backend.orderhere.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.backend.orderhere.dto.ingredient.DeleteIngredientDTO;
import com.backend.orderhere.dto.ingredient.GetIngredientDTO;
import com.backend.orderhere.dto.ingredient.PostIngredientDTO;
import com.backend.orderhere.mapper.LinkIngredientDishMapper;
import com.backend.orderhere.model.Dish;
import com.backend.orderhere.model.Ingredient;
import com.backend.orderhere.model.LinkIngredientDish;
import com.backend.orderhere.repository.DishRepository;
import com.backend.orderhere.repository.IngredientRepository;
import com.backend.orderhere.repository.LinkIngredientDishRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@XRayEnabled
public class LinkIngredientDishService {
    @Autowired
    private LinkIngredientDishRepository linkIngredientDishRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private LinkIngredientDishMapper linkIngredientDishMapper;

    public Integer createLink(PostIngredientDTO addIngredientDTO) {
        Dish dish = dishRepository.findById(addIngredientDTO.getDishId()).orElseThrow(() -> new EntityNotFoundException("Dish not found"));
        Ingredient ingredient = ingredientRepository.findByName(addIngredientDTO.getName()).orElseGet(() -> {
            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(addIngredientDTO.getName());
            newIngredient.setUnit(addIngredientDTO.getUnit());
            return ingredientRepository.save(newIngredient);
        });

        LinkIngredientDish link = new LinkIngredientDish();
        link.setDish(dish);
        link.setIngredient(ingredient);
        link.setQuantityValue(addIngredientDTO.getQuantityValue());
        link.setQuantityUnit(addIngredientDTO.getUnit());
        linkIngredientDishRepository.save(link);

        return ingredient.getIngredientId();
    }

    public List<GetIngredientDTO> findGetIngredientDTOByDishID(Integer dishID) {
        Dish dish = dishRepository.findById(dishID).orElseThrow(() -> new EntityNotFoundException("Dish not found"));
        List<LinkIngredientDish> links = linkIngredientDishRepository.findByDish(dish);
        return links.stream().map(linkIngredientDishMapper::toDto).collect(Collectors.toList());
    }

    public void deleteById(DeleteIngredientDTO deleteIngredientDTO) {
        Dish dish = dishRepository.findById(deleteIngredientDTO.getDishId()).orElseThrow(() -> new EntityNotFoundException("Dish not found"));
        Ingredient ingredient = ingredientRepository.findById(deleteIngredientDTO.getIngredientId()).orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
        LinkIngredientDish link = (LinkIngredientDish) linkIngredientDishRepository.findByDishAndIngredient(dish, ingredient).orElseThrow(() -> new EntityNotFoundException("Link not found"));
        linkIngredientDishRepository.delete(link);
    }


}
