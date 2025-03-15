package com.backend.orderhere.controller.v2;

import com.backend.orderhere.dto.order.*;
import com.backend.orderhere.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderGraphQLController {

    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderGraphQLController.class);

    @Autowired
    private HttpServletRequest request;

    @PreAuthorize("hasRole('sys_admin') or hasRole('driver')")
    @QueryMapping
    public List<OrderGetDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public OrderGetDTO getOrderById(@Argument Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<OrderGetDTO> getUserOrders() {
        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization header for getOrdersByUserId: " + authorizationHeader);
        return orderService.getOrderByUserId(authorizationHeader);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Integer placeOrder(@Argument PlaceOrderDTO placeOrderDTO) {
        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization header for placeOrder: " + authorizationHeader);
        return orderService.PlaceOrder(authorizationHeader, placeOrderDTO).getOrderId();
    }

    @PreAuthorize("hasRole('sys_admin') or hasRole('driver')")
    @MutationMapping
    public UpdateOrderStatusDTO updateOrderStatus(@Argument UpdateOrderStatusDTO updateOrderStatusDTO) {
        return orderService.updateOrderStatus(updateOrderStatusDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public String deleteOrder(@Argument DeleteOrderDTO deleteOrderDTO) {
        try {
            orderService.deleteOrderById(deleteOrderDTO);
            return "Order deleted successfully";
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}

