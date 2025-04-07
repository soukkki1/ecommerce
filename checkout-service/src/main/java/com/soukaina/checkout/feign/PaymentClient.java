package com.soukaina.checkout.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.soukaina.payment.model.Payment;

import java.util.UUID;

@FeignClient(name = "payment-service", url = "http://localhost:8085/ecommerce/payment")
public interface PaymentClient {
    @PostMapping("/process")
    Payment processPayment(@RequestParam UUID orderId, @RequestParam double amount);
}

