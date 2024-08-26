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
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "restaurant")
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "restaurant_id", nullable = false)
  private Integer restaurantId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "contact_number", nullable = false)
  private String contactNumber;

  @Column(name = "abn", nullable = false)
  private String abn;

  @Column(name = "owner_name", nullable = false)
  private String ownerName;

  @Column(name = "owner_mobile", nullable = false)
  private String ownerMobile;

  @Column(name = "owner_address", nullable = false)
  private String ownerAddress;

  @Column(name = "owner_email", nullable = false)
  private String ownerEmail;

  @Column(name = "owner_crn", nullable = false)
  private String ownerCrn;

  @Column(name = "average_rating")
  private BigDecimal averageRating;

  @CreationTimestamp
  @Column(name = "created_time", nullable = false)
  private ZonedDateTime createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OpeningHours> openingHours = new ArrayList<>();
}
