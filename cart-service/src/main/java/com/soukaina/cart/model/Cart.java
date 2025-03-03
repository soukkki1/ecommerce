package com.soukaina.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart implements Serializable {

    @Id
    private UUID id;
    @JsonManagedReference
    @OneToMany(mappedBy = "cart", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

}
