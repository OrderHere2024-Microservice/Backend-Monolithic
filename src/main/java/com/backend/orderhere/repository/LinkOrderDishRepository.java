package com.backend.orderhere.repository;

import com.backend.orderhere.model.LinkOrderDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkOrderDishRepository extends JpaRepository<LinkOrderDish, Integer> {
    List<LinkOrderDish> findByOrderOrderId(Integer orderId);
    void deleteByOrderOrderId(Integer orderId);
}
