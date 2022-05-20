package com.smoothstack.ordermicroservice.controller;

import java.util.List;

import com.smoothstack.ordermicroservice.data.NewOrder;
import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "ufd/order-service")
public class OrderController {
    
    @Autowired
    OrderService orderService;

    @GetMapping(value = "/{userId}/orders/{orderId}")
    public ResponseEntity<OrderInformation> getOrderDetails(@PathVariable Integer userId, @PathVariable Integer orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderDetails(userId, orderId));
    }

    @GetMapping(value = "/{userId}/orders")
    public ResponseEntity<List<OrderInformation>> getOrderHistory(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderHistory(userId));
    }

    @PutMapping(value = "/{userId}/orders/{orderId}/update")
    public ResponseEntity<OrderInformation> updateOrder(
        @PathVariable Integer userId, 
        @PathVariable Integer orderId, 
        @RequestHeader("update") Boolean update,
        @RequestBody NewOrder updatedOrder
        ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(userId, orderId, updatedOrder));
    }

    @PutMapping(value = "/{userId}/orders/{orderId}/cancel")
    public ResponseEntity<OrderInformation> cancelOrder(@PathVariable Integer userId, @PathVariable Integer orderId, @RequestHeader("cancel") Boolean cancel) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(userId, orderId));
    }

    @DeleteMapping(value = "/{userId}/orders/{orderId}/delete")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Integer userId, @PathVariable Integer orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(userId, orderId));
    }
}
