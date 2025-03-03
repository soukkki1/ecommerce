package com.soukaina.cart.service;

import com.soukaina.cart.model.Cart;
import com.soukaina.cart.model.CartItem;
import com.soukaina.cart.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart getCart(UUID userId) {
        return cartRepository.findById(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(userId, new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItemToCart(UUID userId, CartItem item) {
        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + userId));
        item.setCart(cart);
        cart.getItems().add(item);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    public void clearCart(UUID userId) {
        cartRepository.deleteById(userId);
    }
}
