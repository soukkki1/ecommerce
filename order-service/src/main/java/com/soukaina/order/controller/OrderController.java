package com.soukaina.order.controller;

import com.soukaina.order.model.Order;
import com.soukaina.order.model.OrderItem;
import com.soukaina.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private UUID extractUserId(Jwt jwt) {
        return UUID.fromString(jwt.getClaim("sub"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Order order) {


        UUID userId = extractUserId(jwt);
        Order createdOrder = orderService.createOrder(userId, order.getItems());
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable UUID orderId, @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}
