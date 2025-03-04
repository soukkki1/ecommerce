package com.soukaina.order.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // PENDING, SHIPPED, DELIVERED, CANCELED

    private String paymentStatus; // PENDING, PAID, FAILED

    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> items;

    public enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELED
    }
}


