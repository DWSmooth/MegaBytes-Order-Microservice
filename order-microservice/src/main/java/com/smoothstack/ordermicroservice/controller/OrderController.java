package com.smoothstack.ordermicroservice.controller;

import com.smoothstack.ordermicroservice.data.FullOrderDetails;
import com.smoothstack.ordermicroservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "ufd/order-service")
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/orders/{orderId}")
    public ResponseEntity<FullOrderDetails> getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }
}
