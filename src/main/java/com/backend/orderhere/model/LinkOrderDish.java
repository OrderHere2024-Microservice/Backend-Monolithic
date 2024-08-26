package com.backend.orderhere.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "link_order_dish")
public class LinkOrderDish {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "link_order_dish_id", nullable = false)
  private Integer linkOrderDishId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dish_id", nullable = false)
  private Dish dish;

  @Column(name = "dish_quantity", nullable = false)
  private Integer dishQuantity;

  @CreationTimestamp
  @Column(name = "created_time", nullable = false)
  private ZonedDateTime createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;
}

