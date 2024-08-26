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
@Table(name = "user_address")
public class UserAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_address_id", nullable = false, unique = true)
  private Integer userAddressId;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "is_default", nullable = false)
  private Boolean isDefault;

  @CreationTimestamp
  @Column(name = "created_time", nullable = false)
  private ZonedDateTime createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;
}
