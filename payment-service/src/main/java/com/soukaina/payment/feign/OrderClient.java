package com.soukaina.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "order-service", url = "http://localhost:8085/ecommerce/order")
public interface OrderClient {
    @PutMapping("/{orderId}/status")
    void updateOrderPaymentStatus(@PathVariable UUID orderId, @RequestParam String status);
}
