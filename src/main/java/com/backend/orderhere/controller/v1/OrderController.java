package com.backend.orderhere.controller.v1;

import com.backend.orderhere.controller.v2.OrderGraphQLController;
import com.backend.orderhere.dto.ingredient.DeleteIngredientDTO;
import com.backend.orderhere.dto.order.DeleteOrderDTO;
import com.backend.orderhere.dto.order.OrderGetDTO;
import com.backend.orderhere.dto.order.PlaceOrderDTO;
import com.backend.orderhere.dto.order.UpdateOrderStatusDTO;
import com.backend.orderhere.model.Order;
import com.backend.orderhere.model.enums.OrderStatus;
import com.backend.orderhere.model.enums.OrderType;
import com.backend.orderhere.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/public/orders")
@Deprecated
@ConditionalOnMissingBean(OrderGraphQLController.class)
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderGetDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetDTO> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderGetDTO>> getOrdersByUserId(@RequestHeader(name = "Authorization") String authorizationHeader) {
        return ResponseEntity.ok(orderService.getOrderByUserId(authorizationHeader));
    }

    @GetMapping("/status")
    public ResponseEntity<List<OrderGetDTO>> getOrderByOrderStatus(@RequestParam("orderStatus") OrderStatus orderStatus) {
        return ResponseEntity.ok(orderService.getOrderByOrderStatus(orderStatus));
    }

    @GetMapping("/type")
    public ResponseEntity<List<OrderGetDTO>> getOrderByOrderType(@RequestParam("orderType") OrderType orderType) {
        return ResponseEntity.ok(orderService.getOrderByOrderType(orderType));
    }

    @PreAuthorize("hasRole('sys_admin') or hasRole('driver')")
    @PatchMapping("/status")
    public ResponseEntity<UpdateOrderStatusDTO> updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) {
        return ResponseEntity.ok().body(orderService.updateOrderStatus(updateOrderStatusDTO));
    }

    @PostMapping
    public ResponseEntity<Integer> placeOrder(@RequestHeader(name = "Authorization") String authorizationHeader, @RequestBody PlaceOrderDTO placeOrderDTO) {
        Integer orderId = orderService.PlaceOrder(authorizationHeader, placeOrderDTO).getOrderId();
        return ResponseEntity.ok(orderId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOrder(@RequestBody DeleteOrderDTO deleteOrderDTO) {
        try {
            orderService.deleteOrderById(deleteOrderDTO);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

