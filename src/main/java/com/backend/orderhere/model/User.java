package com.backend.orderhere.model;

import com.backend.orderhere.model.enums.UserRole;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, unique = true)
  private Integer userId;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "firstname", nullable = false)
  private String firstname;

  @Column(name = "lastname", nullable = false)
  private String lastname;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "user_email", nullable = false, unique = true)
  private String email;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Column(name = "point")
  private Integer point;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false, columnDefinition = "user_role")
  @Type(PostgreSQLEnumType.class)
  private UserRole userRole;

  @CreationTimestamp
  @Column(name = "created_time", nullable = false)
  private ZonedDateTime createdTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;

  @Column(name = "google_open_id", unique = true)
  private String googleOpenId;

  @Column(name = "facebook_open_id", unique = true)
  private String facebookOpenId;


}
