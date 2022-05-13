/*package com.smoothstack.ordermicroservice.service;

import java.time.LocalDateTime;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserRepository;

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
    UserRepository userRepo;


    @Test
    public void doesServiceGetOrderDetailsById() {
        // Save Order
        Order order = new Order();
        order.setOrderStatus("test status");
        order.setRestaurantNotes("restaurant note");
        order.setDriverNotes("driver note");
        order.setSubTotal(35.00d);
        order.setDeliveryFee(3.00d);
        order.setTax(2.53d);
        order.setTip(5.00d);
        order.setTotal(45.53d);
        LocalDateTime now = LocalDateTime.now();
        order.setTimeCreated(now);
        order.setScheduledFor(now.plusHours(1));

        Order savedOrder = orderRepo.save(order);

        // Save Customer
        User customer = new User();
        userRepo.save(customer);

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
*/