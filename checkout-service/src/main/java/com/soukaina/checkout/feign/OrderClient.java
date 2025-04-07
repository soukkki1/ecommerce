package com.soukaina.checkout.feign;

import com.soukaina.cart.model.CartItem;
import com.soukaina.order.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.UUID;

@FeignClient(name = "order-service", url = "http://localhost:8085/ecommerce/order")
public interface OrderClient {
    @PostMapping("/")
    Order createOrder(@RequestBody List<CartItem> items, @RequestParam double totalPrice);
}

