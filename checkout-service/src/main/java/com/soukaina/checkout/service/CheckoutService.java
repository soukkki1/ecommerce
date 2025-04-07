package com.soukaina.checkout.service;

import com.soukaina.checkout.feign.CartClient;
import com.soukaina.checkout.feign.OrderClient;
import com.soukaina.checkout.feign.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import com.soukaina.cart.model.Cart;
import com.soukaina.order.model.Order;
import com.soukaina.payment.model.Payment;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartClient cartClient;
    private final PaymentClient paymentClient;
    private final OrderClient orderClient;

    public Order checkout(UUID userId, UUID orderId) {
        // Step 1: Retrieve Cart
        Cart cart = cartClient.getCart();
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot proceed with checkout.");
        }

        // Step 2: Create Order
        Order order = orderClient.createOrder(cart.getItems(), cart.getTotalPrice());

        // Step 3: Process Payment
        Payment payment = paymentClient.processPayment(order.getId(), cart.getTotalPrice());
        if (!payment.getStatus().equals(Payment.PaymentStatus.SUCCESS)) {
            throw new IllegalStateException("Payment failed !");
        }

        // Step 4: Clear Cart
        cartClient.clearCart();

        return order;
    }
}
