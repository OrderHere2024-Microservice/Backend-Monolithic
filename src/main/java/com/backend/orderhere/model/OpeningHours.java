package com.backend.orderhere.model;

import com.backend.orderhere.enums.Week;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "opening_hours")
public class OpeningHours {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "opening_hours_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Enumerated(EnumType.STRING)
  @Type(PostgreSQLEnumType.class)
  @Column(name = "day_of_week", nullable = false, columnDefinition = "week")
  private Week dayOfWeek;

  @Column(name = "opening_time", nullable = false)
  private String openingTime;

  @Column(name = "closing_time", nullable = false)
  private String closingTime;

  @UpdateTimestamp
  @Column(name = "updated_time", nullable = false)
  private ZonedDateTime updatedTime;
}
