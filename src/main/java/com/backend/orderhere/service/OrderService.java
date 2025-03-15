package com.backend.orderhere.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.backend.orderhere.auth.KeyCloakService;
import com.backend.orderhere.dto.OrderDishDTO;
import com.backend.orderhere.dto.order.DeleteOrderDTO;
import com.backend.orderhere.dto.order.OrderGetDTO;
import com.backend.orderhere.dto.order.PlaceOrderDTO;
import com.backend.orderhere.dto.order.UpdateOrderStatusDTO;
import com.backend.orderhere.exception.ResourceNotFoundException;
import com.backend.orderhere.auth.JwtUtil;
import com.backend.orderhere.mapper.OrderMapper;
import com.backend.orderhere.model.*;
import com.backend.orderhere.enums.OrderStatus;
import com.backend.orderhere.enums.OrderType;
import com.backend.orderhere.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@XRayEnabled
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;
    private final RestaurantRepository restaurantRepository;
    private final KeyCloakService keyCloakService;
    private final JwtUtil jwtUtil;


    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ObjectMapper objectMapper, RestaurantRepository restaurantRepository, KeyCloakService keyCloakService, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
        this.restaurantRepository = restaurantRepository;
        this.keyCloakService = keyCloakService;
        this.jwtUtil = jwtUtil;
    }

    private List<OrderDishDTO> deserializeOrderDishes(List<OrderDishDTO> orderDishesJson) {
        try {
            // If JSON is already deserialized, return as-is
            if (orderDishesJson == null) return List.of();
            return objectMapper.convertValue(orderDishesJson, new TypeReference<List<OrderDishDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing order dishes", e);
        }
    }

    public List<OrderGetDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAllByIsDeletedFalse();
        return orders.stream().map(order -> {
            OrderGetDTO orderDTO = orderMapper.fromOrderToOrderGetDTO(order, keyCloakService);
            List<OrderDishDTO> dishDTOs = deserializeOrderDishes(order.getOrderDishes());
            orderDTO.setDishes(dishDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    public OrderGetDTO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderGetDTO orderDTO = orderMapper.fromOrderToOrderGetDTO(order, keyCloakService);
        List<OrderDishDTO> dishDTOs = deserializeOrderDishes(order.getOrderDishes());
        orderDTO.setDishes(dishDTOs);

        return orderDTO;
    }

    public List<OrderGetDTO> getOrderByUserId(String token) {
        String userId = (token != null) ? jwtUtil.getUserIdFromToken(token) : null;
        List<Order> orders = orderRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No orders found for the user"));
        return orders.stream().map(order -> {
            OrderGetDTO orderDTO = orderMapper.fromOrderToOrderGetDTO(order, keyCloakService);
            List<OrderDishDTO> dishDTOs = deserializeOrderDishes(order.getOrderDishes());
            orderDTO.setDishes(dishDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    public List<OrderGetDTO> getOrderByOrderStatus(OrderStatus orderStatus) {
        List<Order> orders = orderRepository.findByOrderStatusAndIsDeletedFalse(orderStatus);
        return orders.stream().map(order -> {
            OrderGetDTO orderDTO = orderMapper.fromOrderToOrderGetDTO(order, keyCloakService);
            List<OrderDishDTO> dishDTOs = deserializeOrderDishes(order.getOrderDishes());
            orderDTO.setDishes(dishDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    public List<OrderGetDTO> getOrderByOrderType(OrderType orderType) {
        List<Order> orders = orderRepository.findByOrderTypeAndIsDeletedFalse(orderType);
        return orders.stream().map(order -> {
            OrderGetDTO orderDTO = orderMapper.fromOrderToOrderGetDTO(order, keyCloakService);
            List<OrderDishDTO> dishDTOs = deserializeOrderDishes(order.getOrderDishes());
            orderDTO.setDishes(dishDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public UpdateOrderStatusDTO updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) {

        Order order = orderRepository.findById(updateOrderStatusDTO.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderStatus status = OrderStatus.valueOf(updateOrderStatusDTO.getOrderStatus());
        order.setOrderStatus(status);
        orderRepository.save(order);
        return orderMapper.fromOrdertoUpdateOrderStatusDTO(order);
    }

    public Order PlaceOrder(String token, PlaceOrderDTO placeOrderDTO) {
        String userId = (token != null) ? jwtUtil.getUserIdFromToken(token) : null;
        Order order = orderMapper.dtoToOrder(placeOrderDTO);
        Restaurant restaurant = restaurantRepository.findById(placeOrderDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + placeOrderDTO.getRestaurantId()));
        order.setUserId(userId);
        order.setRestaurant(restaurant);
        List<OrderDishDTO> dishDTOs = placeOrderDTO.getDishes();
        order.setOrderDishes(dishDTOs);
        order = orderRepository.save(order);
        return order;
    }

    @Transactional
    public void deleteOrderById(DeleteOrderDTO deleteOrderDTO) {
        int orderId = deleteOrderDTO.getOrderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setIsDeleted(true);
        orderRepository.save(order);
    }

    @Transactional
    public void markOrderStatusAsPreparing(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setOrderStatus(OrderStatus.preparing);
        orderRepository.save(order);
    }

    @Transactional
    public void markDishAsDeleted(Integer dishId) {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            List<OrderDishDTO> updatedDishes = order.getOrderDishes().stream()
                    .map(dish -> {
                        if (dish.getDishId().equals(dishId)) {
                            dish.setIsDeleted(true);
                        }
                        return dish;
                    })
                    .collect(Collectors.toList());

            order.setOrderDishes(updatedDishes);
            orderRepository.save(order);
        }
    }

}
