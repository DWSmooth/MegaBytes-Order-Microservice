package com.smoothstack.ordermicroservice.controller;

import java.util.List;

import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @GetMapping(value = "ufd/order-service/{userId}/orders/{orderId}")
    public ResponseEntity<OrderInformation> getOrderDetails(@PathVariable Integer userId, @PathVariable Integer orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderDetails(userId, orderId));
    }

    @GetMapping(value = "ufd/order-service/{userId}/orders")
    public ResponseEntity<List<OrderInformation>> getOrderHistory(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderHistory(userId));
    }

    @DeleteMapping(value = "ufd/order-service/{userId}/orders/{orderId}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Integer userId, @PathVariable Integer orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(userId, orderId));
    }
}
