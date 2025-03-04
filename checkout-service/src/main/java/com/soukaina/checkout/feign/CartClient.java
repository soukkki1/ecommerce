package com.soukaina.checkout.feign;

import com.soukaina.cart.model.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cart-service", url = "http://localhost:8085/ecommerce/order")
public interface CartClient {
    @GetMapping
    Cart getCart();

    @DeleteMapping("/clear")
    void clearCart();
}

