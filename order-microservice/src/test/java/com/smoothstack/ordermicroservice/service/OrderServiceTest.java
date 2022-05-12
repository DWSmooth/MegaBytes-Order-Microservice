package com.smoothstack.ordermicroservice.service;

import java.time.LocalDateTime;

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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService service;

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

    @Test
    public void doesServiceGetOrderDetailsById() {
        // Save Order
        Order order = new Order();
        order.setOrder_status("test status");
        order.setRestaurant_notes("restaurant note");
        order.setDriver_notes("driver note");
        order.setSub_total(35.00d);
        order.setDelivery_fee(3.00d);
        order.setTax(2.53d);
        order.setTip(5.00d);
        order.setTotal(45.53d);
        LocalDateTime now = LocalDateTime.now();
        order.setTime_created(now);
        order.setScheduled_for(now.plusHours(1));

        Order savedOrder = orderRepo.save(order);

        // Save Customer
        OrderCustomer customer = new OrderCustomer(0l, savedOrder.getId());
        customerRepo.save(customer);

        // Save Driver
        OrderDriver driver = new OrderDriver(1l, savedOrder.getId());
        driverRepo.save(driver);

        // Save Items
        OrderItem item1 = new OrderItem(savedOrder.getId(), 0l, "", 0.0d, 20.00d);
        OrderItem item2 = new OrderItem(savedOrder.getId(), 1l, "", 0.0d, 10.00d);
        OrderItem item3 = new OrderItem(savedOrder.getId(), 2l, "", 0.0d, 5.00d);
        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);

        // Save Restaurant
        OrderRestaurant restaurant = new OrderRestaurant(0l, savedOrder.getId());
        restaurantRepo.save(restaurant);

        ResponseEntity<FullOrderDetails> createdDetails = service.getOrderDetails(savedOrder.getId());

        System.out.println(createdDetails.getBody().toString());
    }
    
}
