package com.smoothstack.ordermicroservice.controller;

import java.util.List;

import com.smoothstack.ordermicroservice.data.NewOrder;
import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.exceptions.OrderNotCancelableException;
import com.smoothstack.ordermicroservice.exceptions.OrderNotFoundException;
import com.smoothstack.ordermicroservice.exceptions.OrderNotUpdateableException;
import com.smoothstack.ordermicroservice.exceptions.UserMismatchException;
import com.smoothstack.ordermicroservice.exceptions.UserNotFoundException;
import com.smoothstack.ordermicroservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "ufd/order-service")
public class OrderController {
    
    @Autowired
    OrderService orderService;

    // CRUD Mappings

    @GetMapping(value = "/{userId}/orders/{orderId}")
    public ResponseEntity<OrderInformation> getOrderDetails(@PathVariable Integer userId, @PathVariable Integer orderId) 
    throws OrderNotFoundException, UserMismatchException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderDetails(userId, orderId));
    }

    @GetMapping(value = "/{userId}/orders")
    public ResponseEntity<List<OrderInformation>> getOrderHistory(@PathVariable Integer userId) 
    throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderHistory(userId));
    }

    @PutMapping(value = "/{userId}/orders/{orderId}/update")
    public ResponseEntity<OrderInformation> updateOrder(
        @PathVariable Integer userId, 
        @PathVariable Integer orderId, 
        @RequestHeader("update") Boolean update,
        @RequestBody NewOrder updatedOrder
        )
    throws OrderNotFoundException, OrderNotUpdateableException, UserMismatchException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(userId, orderId, updatedOrder));
    }

    @PutMapping(value = "/{userId}/orders/{orderId}/cancel")
    public ResponseEntity<OrderInformation> cancelOrder(@PathVariable Integer userId, @PathVariable Integer orderId, @RequestHeader("cancel") Boolean cancel) 
    throws OrderNotFoundException, OrderNotCancelableException, UserMismatchException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(userId, orderId));
    }

    @DeleteMapping(value = "/{userId}/orders/{orderId}/delete")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Integer userId, @PathVariable Integer orderId)
    throws OrderNotFoundException, UserMismatchException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(userId, orderId));
    }

    // Exception Handlers

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> orderNotFoundException(Throwable err) {
        return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotCancelableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> orderNotCancelableException(Throwable err) {
        return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotUpdateableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> orderNotUpdateableException(Throwable err) {
        return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> userMismatchException(Throwable err) {
        return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> userNotFoundException(Throwable err) {
        return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
    }
}
