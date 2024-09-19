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
public class OrderGraphQLControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllOrders() throws Exception {
        String query = "{ getAllOrders { orderId restaurantId totalPrice orderStatus } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrderById() throws Exception {
        String query = "{ getOrderById(orderId: 1) { orderId totalPrice orderStatus } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        String query = "{ getOrdersByUserId { orderId totalPrice } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        // we need to generate a recent JWT for the test that we can test further: e.g. response data...
                        // static JWT will expired over time
                        // .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGV2ZW5AZW1haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfY3VzdG9tZXIifV0sInVzZXJJZCI6MywiYXZhdGFyVVJMIjoiU09NRV9ERUZBVUxUX1VSTCIsInVzZXJOYW1lIjoiU3RldmVuIEd1byIsImlhdCI6MTcyNjU3NzkzOSwiZXhwIjoxNzI2NjY0MzM5fQ.VvonD4HJZ0Qoz6vLuMltPb7jV7eP-vIqel20UlPCVqo")
                        .content("{\"query\":\"" + query + "\"}"))
                .andExpect(status().isOk());
    }


    @Test
    void testPlaceOrderWithoutAdminRole() throws Exception {
        String mutation = "mutation { placeOrder(placeOrderDTO: { "
                + "restaurantId: 1, "
                + "tableNumber: 5, "
                + "orderType: dine_in, "
                + "orderStatus: pending, "
                + "discount: 10.00, "
                + "dishes: [{ dishId: 1, dishName: \\\"Pizza\\\", dishQuantity: 2, dishPrice: 20.00 }], "
                + "totalPrice: 40.00, "
                + "note: \\\"Extra cheese\\\", "
                + "address: \\\"123 Main St\\\", "
                + "phone: \\\"0412345678\\\", "
                + "numberOfPeople: 2, "
                + "pickupTime: \\\"2024-09-17T12:00:00Z\\\" }) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized"));
    }


    @Test
    void testUpdateOrderStatusWithoutAdminRole() throws Exception {
        String mutation = "mutation { updateOrderStatus(updateOrderStatusDTO: { "
                + "orderId: 1, "
                + "orderStatus: finished }) { orderId orderStatus } }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$.errors[0].message").value("Unauthorized"));
    }

    @Test
    void testDeleteOrderWithoutAdminRole() throws Exception {
        String mutation = "mutation { deleteOrder(deleteOrderDTO: { orderId: 1 }) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$.errors[0].message").value("Unauthorized"));
    }
}
