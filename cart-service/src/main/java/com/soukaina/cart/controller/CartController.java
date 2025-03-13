package com.soukaina.cart.controller;

import com.soukaina.cart.model.Cart;
import com.soukaina.cart.model.CartItem;
import com.soukaina.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@RestController
@RequestMapping("/ecommerce/cart")
@RequiredArgsConstructor
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    private final CartService cartService;

    private UUID extractUserId(Jwt jwt) {
        return UUID.fromString(jwt.getClaim("sub"));
    }

    @GetMapping
    public Cart getCart(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        logger.info("getCart from userId {}", userId);
        return cartService.getCart(userId);
    }

    @PostMapping("/add")
    public Cart addItem(@AuthenticationPrincipal Jwt jwt, @RequestBody CartItem item) {
        UUID userId = extractUserId(jwt);
        return cartService.addItemToCart(userId, item);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart removeItem(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID productId) {
        UUID userId = extractUserId(jwt);
        return cartService.removeItemFromCart(userId, productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        cartService.clearCart(userId);
    }
}
