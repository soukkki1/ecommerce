package com.soukaina.product.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;
}

