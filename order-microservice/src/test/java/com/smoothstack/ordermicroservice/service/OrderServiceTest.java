package com.smoothstack.ordermicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserInformationRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.common.services.CommonLibraryTestingService;
import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.exceptions.OrderNotFoundException;
import com.smoothstack.ordermicroservice.exceptions.UserMismatchException;
import com.smoothstack.ordermicroservice.exceptions.UserNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService service;

    @Autowired
    CommonLibraryTestingService testingService;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserInformationRepository userInfoRepo;

    @Autowired
    OrderItemRepository orderItemRepo;

    @BeforeEach
    public void setUpTestEnvironment() {
        testingService.createTestData();
    }

    // Test getOrderDetails

    @Test
    public void doesServiceGetOrderDetailsById() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        
        try {
            OrderInformation orderInfo = service.getOrderDetails(testUser.getId(), 1);
            assertEquals("Per", orderInfo.getDriverFirstName());
            assertEquals("Dublin Bay Irish Pub & Grill", orderInfo.getRestaurantNames().get(0));
        } catch (OrderNotFoundException | UserMismatchException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doesGetOrderDetailsThrowOrderNotFound() {
        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwOrderNotFound = false;
        boolean threwUserMismatch = false;
        try {
            service.getOrderDetails(testUser.getId(), 100);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }
        assertTrue(threwOrderNotFound);
        assertFalse(threwUserMismatch);
    }

    @Test
    public void doesGetOrderDetailsThrowUserMismatch() {
        boolean threwOrderNotFound = false;
        boolean threwUserMismatch = false;
        try {
            service.getOrderDetails(100, 1);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }
        assertFalse(threwOrderNotFound);
        assertTrue(threwUserMismatch);
    }

    // Test getOrderHistory

    @Test
    public void doesServiceGetOrderHistory() {
        
        User testUser = userRepo.findTopByUserName("testCustomer").get();
        List<OrderInformation> orders = new ArrayList<>();
        boolean threwUserNotFound = false;

        try {
            orders = service.getOrderHistory(testUser.getId());

        } catch (UserNotFoundException e) {
            threwUserNotFound = true;
        }

        assertFalse(threwUserNotFound);
        assertEquals(2, orders.size());
        assertEquals("Dublin Bay Irish Pub & Grill", orders.get(0).getRestaurantNames().get(0));
        assertEquals("Tropical Smoothie Cafe", orders.get(1).getRestaurantNames().get(0));
    }

    @Test
    public void doesGetOrderHistoryThrowUserNotFound() {
        boolean threwUserNotFound = false;

        try {
            service.getOrderHistory(100);
        } catch (UserNotFoundException e) {
            threwUserNotFound = true;
        }

        assertTrue(threwUserNotFound);
    }
    
}