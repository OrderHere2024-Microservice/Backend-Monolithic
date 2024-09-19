package com.backend.orderhere.controller.v2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DishGraphQLControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateDishWithoutAdminRole() throws Exception {
        String mutation = "mutation { createDish(dishCreateDto: { "
                + "dishName: \\\"Test Dish\\\", "
                + "description: \\\"A delicious test dish\\\", "
                + "price: 9.99, "
                + "imageUrl: \\\"test.jpg\\\", "
                + "restaurantId: 1, "
                + "availability: true, "
                + "categoryId: 2 }) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));
    }

    @Test
    void testUpdateDishWithoutAdminRole() throws Exception {
        String mutation = "mutation { updateDish(dishUpdateDto: { "
                + "dishId: 1, "
                + "dishName: \\\"Updated Dish\\\", "
                + "description: \\\"Updated description\\\", "
                + "price: 15.99, "
                + "imageUrl: \\\"updated.jpg\\\", "
                + "restaurantId: 1, "
                + "availability: true }) { dishId dishName description price } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));
    }

    @Test
    void testDeleteDishWithoutAdminRole() throws Exception {
        String mutation = "mutation { deleteDish(dishId: 1) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));
    }

    @Test
    void testGetDishes() throws Exception {
        String query = "{ getDishes(restaurantId: 1, page: 1, size: 10, sort: \\\"price\\\", order: \\\"asc\\\") { "
                + "data { dishId dishName price } "
                + "totalItems totalPages currentPage } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getDishes.data").isArray())
                .andExpect(jsonPath("$.data.getDishes.totalItems").exists())
                .andExpect(jsonPath("$.data.getDishes.totalPages").exists())
                .andExpect(jsonPath("$.data.getDishes.currentPage").value(1));
    }
}
