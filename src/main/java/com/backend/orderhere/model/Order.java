package com.backend.orderhere.model;

import com.backend.orderhere.model.enums.OrderStatus;
import com.backend.orderhere.model.enums.OrderType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Integer orderId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Enumerated(EnumType.STRING)
  @Type(PostgreSQLEnumType.class)
  @Column(name = "order_status", nullable = false, columnDefinition = "order_status")
  private OrderStatus orderStatus;

  @Enumerated(EnumType.STRING)
  @Type(PostgreSQLEnumType.class)
  @Column(name = "order_type", nullable = false, columnDefinition = "order_type")
  private OrderType orderType;

  @Column(name = "table_number")
  private Integer tableNumber;

  @Column(name = "number_of_people")
  private Integer numberOfPeople;

  @Column(name = "pickup_time")
  private ZonedDateTime pickupTime;

  @Column(name = "address")
  private String address;

  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;

  @Column(name = "discount", nullable = false)
  private BigDecimal discount;

  @Column(name = "note")
  private String note;

  @Column(name = "phone")
  private String phone;

  @CreationTimestamp
  @Column(name = "created_time", nullable = false)
  private ZonedDateTime createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;
}
