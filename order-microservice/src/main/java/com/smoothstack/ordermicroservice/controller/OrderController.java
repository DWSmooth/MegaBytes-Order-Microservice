package com.smoothstack.ordermicroservice.controller;

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

    @RequestMapping(value = "ufd/order-service/orders/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<OrderInformation> getOrderDetails(@PathVariable Integer orderId) {
        System.out.println("In order details controller method");
        return orderService.getOrderDetails(orderId);
    }
}
