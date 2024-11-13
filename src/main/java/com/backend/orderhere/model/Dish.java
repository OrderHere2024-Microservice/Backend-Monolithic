package com.backend.orderhere.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "dish")
public class Dish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "dish_id", nullable = false)
  private Integer dishId;

  @Column(name = "dish_name", nullable = false)
  private String dishName;

  @Column(name = "description", nullable = true)
  private String description;

  @Column(name = "price", nullable = false, precision = 5, scale = 2)
  private BigDecimal price;

  @Column(name = "image_url", nullable = true)
  private String imageUrl;

  @Column(name = "rating", nullable = true, precision = 3, scale = 1)
  private BigDecimal rating;

  @Column(name = "restaurant_id", nullable = false)
  private Integer restaurantId;

  @Column(name = "availability", nullable = false)
  private Boolean availability;

  @CreationTimestamp
  @Column(name = "created_time", nullable = false)
  private ZonedDateTime createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<LinkIngredientDish> linkIngredientDishes;

  @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Rating> ratings;

  @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<LinkOrderDish> linkOrderDishes;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted;
}