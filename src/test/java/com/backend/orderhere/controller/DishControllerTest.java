package com.backend.orderhere.controller;

import com.backend.orderhere.controller.v1.DishController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DishController.class)
public class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    static class TestConfig {
    }

    @Test
    void testGetDishesWithInvalidRestaurantId() throws Exception {
        mockMvc.perform(get("/v1/public/dish/11111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // fix this Unauthorized in the future
    }

    @Test
    void testGetDishesByCategoryNotFound() throws Exception {
        mockMvc.perform(get("/v1/public/dish/9999/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // fix this Unauthorized in the future
    }
}
