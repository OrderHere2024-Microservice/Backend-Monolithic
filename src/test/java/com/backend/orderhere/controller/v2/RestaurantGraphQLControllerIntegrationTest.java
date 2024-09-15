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
public class RestaurantGraphQLControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateRestaurantWithoutAdminRole() throws Exception {
        String mutation = "mutation { createRestaurant(restaurantCreateDTO: { "
                + "name: \\\"New Restaurant\\\", "
                + "description: \\\"Test description\\\", "
                + "address: \\\"123 Main St\\\", "
                + "contactNumber: \\\"0412345678\\\", "
                + "abn: \\\"12345678901\\\", "
                + "ownerName: \\\"John Doe\\\", "
                + "ownerMobile: \\\"0412345678\\\", "
                + "ownerAddress: \\\"John Doe St\\\", "
                + "ownerCrn: \\\"adfa\\\","
                + "ownerEmail: \\\"john.doe@example.com\\\" }) { "
                + "restaurantId name } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$.errors[0].message").value("Unauthorized")); // it will return 200 even it is unauthorized
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        String query = "{ getAllRestaurants { restaurantId name description } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRestaurantById() throws Exception {
        String query = "{ getRestaurantById(restaurantId: 1) { restaurantId name } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk());
    }
}
