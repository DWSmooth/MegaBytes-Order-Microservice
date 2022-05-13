package com.smoothstack.ordermicroservice.controller;

import java.util.List;

import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "ufd/order-service/{userId}/orders/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<OrderInformation> getOrderDetails(@PathVariable Integer userId, @PathVariable Integer orderId) {
        return orderService.getOrderDetails(userId, orderId);
    }

    @RequestMapping(value = "ufd/order-service/{userId}/orders", method = RequestMethod.GET)
    public ResponseEntity<List<OrderInformation>> getOrderDetails(@PathVariable Integer userId) {
        return orderService.getOrderHistory(userId);
    }
}
