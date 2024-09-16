package com.backend.orderhere.model;

import com.backend.orderhere.model.enums.PaymentStatus;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
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
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, columnDefinition = "payment_status")
    @Type(PostgreSQLEnumType.class)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "stripe_payment_id", nullable = false)
    private String stripePaymentId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @CreationTimestamp
    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private ZonedDateTime updatedTime;
}
