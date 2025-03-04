package com.soukaina.payment.controller;

import com.nimbusds.jwt.JWT;
import com.soukaina.payment.model.Payment;
import com.soukaina.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/ecommerce/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private UUID extractUserId(Jwt jwt) {
        return UUID.fromString(jwt.getClaim("sub"));
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(
            @AuthenticationPrincipal Jwt jwt, @RequestParam UUID orderId, @RequestParam double amount) {
        UUID userId = extractUserId(jwt);
        return ResponseEntity.ok(paymentService.processPayment(userId, orderId, amount));
    }
}

