package com.smoothstack.ordermicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderCustomerRepository customerRepo;

    @Autowired
    OrderDriverRepository driverRepo;

    @Autowired
    OrderItemRepository itemRepo;

    @Autowired
    OrderRestaurantRepository restaurantRepo;
    
    public Integer getOrderDetails(Long orderId) {
        
        
    }
}
