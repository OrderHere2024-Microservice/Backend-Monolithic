package com.backend.orderhere.repository;

import com.backend.orderhere.model.Order;
import com.backend.orderhere.model.enums.OrderStatus;
import com.backend.orderhere.model.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

  List<Order> findByOrderStatus(OrderStatus orderStatus);

  List<Order> findByOrderType(OrderType orderType);

  List<Order> findByUserUserId(Integer userId);

  Order findByOrderId(Integer orderId);
}
