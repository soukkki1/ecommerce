package com.soukaina.order.service;

import com.soukaina.order.model.Order;
import com.soukaina.order.model.OrderItem;
import com.soukaina.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // Create Order
    public Order createOrder(UUID userId, List<OrderItem> items ) {
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setPaymentStatus("PAID");
        order.setOrderStatus(Order.OrderStatus.PENDING);
        double totalPrice = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    // Get Order by ID
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    // Get Orders by User
    public List<Order> getOrdersByUser(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    // Update Order Status
    public Order updateOrderStatus(UUID orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
}
