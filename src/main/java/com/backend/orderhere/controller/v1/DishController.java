package com.backend.orderhere.controller.v1;

import com.backend.orderhere.annotations.TrackTime;
import com.backend.orderhere.dto.PagingDto;
import com.backend.orderhere.dto.dish.DishCreateDto;
import com.backend.orderhere.dto.dish.DishGetDto;
import com.backend.orderhere.dto.dish.DishUpdateDTO;
import com.backend.orderhere.service.DishService;
import com.backend.orderhere.service.enums.DishSort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.orderhere.util.SortHelper.getSortOrder;

@RestController
@RequestMapping("/v1/public/dish")
@RequiredArgsConstructor
@Validated
public class DishController {
    private final DishService dishService;

    @GetMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    @TrackTime
    @Deprecated
    public PagingDto<List<DishGetDto>> getDishes(@PathVariable Integer restaurantId,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "0") int size,
                                                 @RequestParam(defaultValue = "category") String sort,
                                                 @RequestParam(defaultValue = "asc") String order) {
        return dishService.getDishPageByRestaurantId(
                restaurantId,
                page,
                size,
                DishSort.getEnumByString(sort),
                getSortOrder(order)
        );
    }

    // Not used
    @GetMapping("/{restaurantId}/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DishGetDto> getDishesByCategory(@PathVariable Integer restaurantId,
                                                @PathVariable Integer categoryId) {
        return dishService.getDishByCategory(restaurantId, categoryId);
    }

    // still in use, will deprecate this when solving gql upload issue
    @PreAuthorize("hasRole('sys_admin')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createDish(@Valid @ModelAttribute DishCreateDto dishCreateDto) {
        dishService.createDish(dishCreateDto);
    }

    // still in use, will deprecate this when solving gql upload issue
    @PreAuthorize("hasRole('sys_admin')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DishGetDto updateDish(
            @Valid @ModelAttribute DishUpdateDTO dishUpdateDto) {
        return dishService.updateDish(dishUpdateDto);
    }

    @PreAuthorize("hasRole('sys_admin')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/1/{dishId}")
    @Deprecated
    public void deleteDish(@PathVariable Integer dishId) throws Exception {

        dishService.deleteDish(dishId);
    }
}
