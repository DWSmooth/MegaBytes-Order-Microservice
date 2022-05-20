package com.smoothstack.ordermicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserInformationRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.common.services.CommonLibraryTestingService;
import com.smoothstack.ordermicroservice.data.OrderInformation;

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

    @Test
    public void doesServiceGetOrderDetailsById() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        
        OrderInformation orderInfo = service.getOrderDetails(testUser.getId(), 1);

        assertEquals("Per", orderInfo.getDriverFirstName());
        assertEquals("Dublin Bay Irish Pub & Grill", orderInfo.getRestaurantNames().get(0));
        
    }
    
}