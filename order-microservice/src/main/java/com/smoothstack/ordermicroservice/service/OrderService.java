package com.smoothstack.ordermicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.smoothstack.ordermicroservice.data.FullOrderDetails;
import com.smoothstack.ordermicroservice.data.Order;
import com.smoothstack.ordermicroservice.data.OrderCustomer;
import com.smoothstack.ordermicroservice.data.OrderDriver;
import com.smoothstack.ordermicroservice.data.OrderItem;
import com.smoothstack.ordermicroservice.data.OrderRestaurant;
import com.smoothstack.ordermicroservice.repository.OrderCustomerRepository;
import com.smoothstack.ordermicroservice.repository.OrderDriverRepository;
import com.smoothstack.ordermicroservice.repository.OrderItemRepository;
import com.smoothstack.ordermicroservice.repository.OrderRepository;
import com.smoothstack.ordermicroservice.repository.OrderRestaurantRepository;

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
    
    public ResponseEntity<FullOrderDetails> getOrderDetails(Long orderId) {
        FullOrderDetails details = new FullOrderDetails();
        
        // Get Order
        Optional<Order> order = Optional.empty();
        
        try {
             order = orderRepo.findById(orderId);
        } catch (Exception e) {
            //TODO: Look up all the exceptions mysql can throw, catch them specifically
        }

        if(order.isPresent()) {
            details.setOrder(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Get Customer Id
        Optional<OrderCustomer> customer = Optional.empty();

        try {
            customer = customerRepo.findByOrder_id(details.getOrder().getId());
        } catch (Exception e) {
            //TODO: Handle Exceptions
        }

        if(customer.isPresent()) {
           details.setCustomerId(customer.get().getCustomer_id());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Get Driver Id
        Optional<OrderDriver> driver = Optional.empty();

        try {
            driver = driverRepo.findByOrder_id(details.getOrder().getId());
        } catch (Exception e) {
            //TODO: Handle Exceptions
        }

        if(driver.isPresent()) {
           details.setDriverId(driver.get().getDriver_id());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Get Restaurant Ids
        Optional<List<OrderRestaurant>> restaurants = Optional.empty();

        try {
            restaurants = restaurantRepo.findAllByOrder_id(details.getOrder().getId());
        } catch (Exception e) {
            //TODO: Handle Exceptions
        }

        if(restaurants.isPresent()) {
            List<Long> restaurantIds = new ArrayList<>();
            for(OrderRestaurant r: restaurants.get()) {
                restaurantIds.add(r.getRestaurant_id());
            }
            details.setRestaurantIds(restaurantIds);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Get Item Ids
        Optional<List<OrderItem>> items = Optional.empty();

        try {
            items = itemRepo.findAllByOrder_id(details.getOrder().getId());
        } catch (Exception e) {
            //TODO: Handle Exceptions
        }

        if(items.isPresent()) {
            List<Long> itemIds = new ArrayList<>();
            for(OrderItem i: items.get()) {
                itemIds.add(i.getMenu_items_id());
            }
            details.setItemIds(itemIds);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(details);
    }
}
