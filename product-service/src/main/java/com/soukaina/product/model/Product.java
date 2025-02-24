package com.soukaina.product.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private int stockQuantity;
    @Column(nullable = true)
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column
    private double discountPrice;
    @Column
    private boolean isOnSale;
}
