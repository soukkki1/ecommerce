package com.soukaina.payment.service;

import com.soukaina.payment.feign.OrderClient;
import com.soukaina.payment.model.Payment;
import com.soukaina.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient; // Calls Order Service

    public Payment processPayment(UUID userId, UUID orderId, double amount) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        // Simulate payment success
        payment.setStatus(Payment.PaymentStatus.SUCCESS);

        Payment savedPayment = paymentRepository.save(payment);

        // Notify OrderService that payment was successful
        orderClient.updateOrderPaymentStatus(orderId, "PAID");

        return savedPayment;
    }
}
