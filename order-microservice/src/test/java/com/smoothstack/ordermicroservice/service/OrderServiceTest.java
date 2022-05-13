package com.smoothstack.ordermicroservice.service;

import java.time.LocalDateTime;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.OrderItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.User;
import com.smoothstack.common.models.UserInformation;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserInformationRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.ordermicroservice.data.OrderInformation;

import org.junit.jupiter.api.Disabled;
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

    @Autowired
    UserInformationRepository userInfoRepo;

    @Autowired
    OrderItemRepository orderItemRepo;


    @Test
    @Disabled
    public void doesServiceGetOrderDetailsById() {
        //Save Customer Information
        UserInformation userInfo = new UserInformation();
        userInfo.setId(0);
        userInfo.setFirstName("John");
        userInfo.setLastName("Smith");
        UserInformation savedUserInfo = userInfoRepo.save(userInfo);
        
        // Save Customer
        User user = new User();
        user.setId(0);
        user.setUserName("testUser");
        user.setUserInformation(savedUserInfo);
        User savedUser = userRepo.save(user);
        
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
        order.setCustomer(savedUser);
        Order savedOrder = orderRepo.save(order);

        

        // Save Driver

        // Save Items
        //OrderItem item1 = new OrderItem(savedOrder.getId(), 0l, "", 0.0d, 20.00d);
        //OrderItem item2 = new OrderItem(savedOrder.getId(), 1l, "", 0.0d, 10.00d);
        //OrderItem item3 = new OrderItem(savedOrder.getId(), 2l, "", 0.0d, 5.00d);
        //orderItemRepo.save(item1);
        //orderItemRepo.save(item2);
        //orderItemRepo.save(item3);

        // Save Restaurant

        OrderInformation createdDetails = service.getOrderDetails(savedUser.getId(), savedOrder.getId());

        System.out.println(createdDetails.toString());
    }
    
}