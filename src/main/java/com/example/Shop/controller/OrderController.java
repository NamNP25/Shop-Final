package com.example.Shop.controller;

import com.example.Shop.dto.OrderRequest;
import com.example.Shop.entity.Order;
import com.example.Shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) { // Phải là OrderRequest
        return ResponseEntity.ok(orderService.placeOrder(request));
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}