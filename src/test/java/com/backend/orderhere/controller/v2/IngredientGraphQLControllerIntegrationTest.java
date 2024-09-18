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
public class IngredientGraphQLControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateLinkIngredientDishWithoutAdminRole() throws Exception {
        String mutation = "mutation { createLinkIngredientDish(postIngredientDTO: { "
                + "dishId: 1, "
                + "name: \\\"Tomato\\\", "
                + "unit: \\\"kg\\\", "
                + "quantityValue: 1.5 }) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));
    }

    @Test
    void testGetAllIngredients() throws Exception {
        String query = "{ getAllIngredients { ingredientId name } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getAllIngredients").isArray());
    }

    @Test
    void testGetIngredientById() throws Exception {
        String query = "{ getIngredientById(id: 1) { ingredientId name } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getIngredientById.ingredientId").value(1))
                .andExpect(jsonPath("$.data.getIngredientById.name").isNotEmpty());
    }

    @Test
    void testFindIngredientsByDishID() throws Exception {
        String query = "{ findIngredientsByDishID(dishID: 1) { linkIngredientDishId dishId ingredientId quantityValue quantityUnit } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.findIngredientsByDishID").isArray());
    }

    @Test
    void testDeleteIngredientLinkWithoutAdminRole() throws Exception {
        String mutation = "mutation { deleteIngredientLink(deleteIngredientDTO: { "
                + "dishId: 1, "
                + "ingredientId: 1 }) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));  // Assuming this mutation requires sys_admin role
    }

    @Test
    void testUpdateIngredientWithoutAdminRole() throws Exception {
        String mutation = "mutation { updateIngredient(updateIngredientDTO: { "
                + "ingredientId: 1, "
                + "name: \\\"Updated Ingredient Name\\\" }) { ingredientId name } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));  // Assuming this mutation requires sys_admin role
    }
}