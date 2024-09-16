package com.backend.orderhere.controller.v2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentGraphQLControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreatePayment() throws Exception {
        String mutation = "mutation { createPayment(paymentPostDto: { "
                + "orderId: 1, "
                + "amount: 100.50, "
                + "currency: \\\"USD\\\" }) { "
                + "paymentId clientSecret } }";  // New clientSecret field

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPaymentResult() throws Exception {
        String mutation = "mutation { getPaymentResult(paymentResultDto: { "
                + "paymentId: \\\"pay_1JYq74Ixq8gfa\\\", "
                + "orderId: 1 }) }";

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"" + mutation + "\"}"))
                .andExpect(status().isOk());
    }
}
