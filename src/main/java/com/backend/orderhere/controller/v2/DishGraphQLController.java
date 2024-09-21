package com.backend.orderhere.controller.v2;

import com.backend.orderhere.dto.PagingDto;
import com.backend.orderhere.dto.dish.DishCreateDto;
import com.backend.orderhere.dto.dish.DishGetDto;
import com.backend.orderhere.dto.dish.DishUpdateDTO;
import com.backend.orderhere.service.DishService;
import com.backend.orderhere.service.enums.DishSort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.backend.orderhere.util.SortHelper.getSortOrder;

@Controller
@RequiredArgsConstructor
public class DishGraphQLController {

    private final DishService dishService;

    @QueryMapping
    public PagingDto<List<DishGetDto>> getDishes(@Argument Integer restaurantId,
                                                 @Argument int page,
                                                 @Argument int size,
                                                 @Argument String sort,
                                                 @Argument String order) {
        return dishService.getDishPageByRestaurantId(
                restaurantId,
                page,
                size,
                DishSort.getEnumByString(sort),
                getSortOrder(order)
        );
    }

    // Currently not using this method as Upload is not working for the scalar
    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public Boolean createDish(@Valid @Argument DishCreateDto dishCreateDto) {
        dishService.createDish(dishCreateDto);
        return true;
    }

    // Currently not using this method as Upload is not working for the scalar
    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public DishGetDto updateDish(@Valid @Argument DishUpdateDTO dishUpdateDto) {
        return dishService.updateDish(dishUpdateDto);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @MutationMapping
    public Boolean deleteDish(@Argument Integer dishId) throws Exception {
        dishService.deleteDish(dishId);
        return true;
    }

}
