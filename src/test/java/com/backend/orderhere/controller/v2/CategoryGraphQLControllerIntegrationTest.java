package com.backend.orderhere.controller.v2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryGraphQLControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCategories() throws Exception {
        String query = "{ getCategories(restaurantId: 1) { categoryId categoryName } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCategoryWithoutAdminRole() throws Exception {
        String mutation = "mutation { createCategory(categoryPostDTO: { "
                + "restaurantId: 1, "
                + "categoryName: \\\"New Category\\\" }) { "
                + "categoryId categoryName } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$.errors[0].message").value("Unauthorized"));
    }
}
