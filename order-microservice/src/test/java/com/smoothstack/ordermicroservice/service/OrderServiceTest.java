package com.smoothstack.ordermicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserInformationRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.common.services.CommonLibraryTestingService;
import com.smoothstack.ordermicroservice.data.NewOrder;
import com.smoothstack.ordermicroservice.data.NewOrderItem;
import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.exceptions.OrderNotCancelableException;
import com.smoothstack.ordermicroservice.exceptions.OrderNotFoundException;
import com.smoothstack.ordermicroservice.exceptions.OrderNotUpdateableException;
import com.smoothstack.ordermicroservice.exceptions.UserMismatchException;
import com.smoothstack.ordermicroservice.exceptions.UserNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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

    @Autowired
    MenuItemRepository menuItemRepo;

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
            e.printStackTrace();
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

    // Test cancelOrder

    @Test
    @DirtiesContext
    public void doesServiceCancelOrder() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwException = false;

        Order orderToDelete = orderRepo.findById(1).get();
        orderToDelete.setOrderStatus("placed");
        orderRepo.save(orderToDelete);

        try {
            service.cancelOrder(testUser.getId(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            threwException = true;
        }

        assertFalse(threwException);
        assertEquals("canceled", orderRepo.findById(1).get().getOrderStatus());

    }

    @Test
    @DirtiesContext
    public void doesCancelOrderThrowOrderNotFound() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwOrderNotFound = false;
        boolean threwOrderNotCancelable = false;
        boolean threwUserMismatch = false;
        
        try {
            service.cancelOrder(testUser.getId(), 100);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (OrderNotCancelableException e) {
            threwOrderNotCancelable = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }

        assertTrue(threwOrderNotFound);
        assertFalse(threwOrderNotCancelable);
        assertFalse(threwUserMismatch);

    }

    @Test
    @DirtiesContext
    public void doesCancelOrderThrowOrderNotCancelable() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwOrderNotFound = false;
        boolean threwOrderNotCancelable = false;
        boolean threwUserMismatch = false;
        
        try {
            System.out.println(orderRepo.findById(1).get().getOrderStatus());
            service.cancelOrder(testUser.getId(), 1);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (OrderNotCancelableException e) {
            threwOrderNotCancelable = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }

        assertFalse(threwOrderNotFound);
        assertTrue(threwOrderNotCancelable);
        assertFalse(threwUserMismatch);

    }

    @Test
    @DirtiesContext
    public void doesCancelOrderThrowUserMismatch() {

        User testUser = userRepo.findTopByUserName("testDriver").get();
        boolean threwOrderNotFound = false;
        boolean threwOrderNotCancelable = false;
        boolean threwUserMismatch = false;

        Order orderToDelete = orderRepo.findById(1).get();
        orderToDelete.setOrderStatus("placed");
        orderRepo.save(orderToDelete);
        
        try {
            service.cancelOrder(testUser.getId(), 1);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (OrderNotCancelableException e) {
            threwOrderNotCancelable = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }

        assertFalse(threwOrderNotFound);
        assertFalse(threwOrderNotCancelable);
        assertTrue(threwUserMismatch);

    }

    // Test updateOrder

    @Test
    @DirtiesContext
    public void doesServiceUpdateOrder() {
        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwException = false;

        NewOrder info = new NewOrder();
        info.setDriverNotes("New driver notes!");

        List<NewOrderItem> items = new ArrayList<>();
        List<Integer> emptyList = new ArrayList<>();

        MenuItem orderItem1 = menuItemRepo.findTopByName("Guinness BBQ Burger").get();
        NewOrderItem item1 = new NewOrderItem(orderItem1.getId(), "", 0.00d, orderItem1.getPrice(), emptyList);
        items.add(item1);

        MenuItem orderItem2 = menuItemRepo.findTopByName("Corned Beef & Cabbage").get();
        NewOrderItem item2 = new NewOrderItem(orderItem2.getId(), "", 0.00d, orderItem2.getPrice(), emptyList);
        items.add(item2);

        info.setItems(items);

        Order orderToUpdate = orderRepo.findById(1).get();
        orderToUpdate.setOrderStatus("placed");
        orderRepo.save(orderToUpdate);

        OrderInformation updatedOrder  = new OrderInformation();

        try {
            updatedOrder = service.updateOrder(testUser.getId(), 1, info);
        } catch (Exception e) {
            e.printStackTrace();
            threwException = true;
        }

        assertFalse(threwException);
        assertEquals("New driver notes!", updatedOrder.getDriverNotes());
        assertEquals("Guinness BBQ Burger", updatedOrder.getItems().get(0).getName());
        assertEquals("Corned Beef & Cabbage", updatedOrder.getItems().get(1).getName());
    }

    @Test
    @DirtiesContext
    public void doesUpdateOrderThrowOrderNotFound() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwOrderNotFound = false;
        boolean threwOrderNotUpdateable = false;
        boolean threwUserMismatch = false;

        NewOrder updates = new NewOrder();
        
        try {
            service.updateOrder(testUser.getId(), 100, updates);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (OrderNotUpdateableException e) {
            threwOrderNotUpdateable = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }

        assertTrue(threwOrderNotFound);
        assertFalse(threwOrderNotUpdateable);
        assertFalse(threwUserMismatch);

    }

    @Test
    @DirtiesContext
    public void doesUpdateOrderThrowOrderNotUpdateable() {

        User testUser = userRepo.findTopByUserName("testCustomer").get();
        boolean threwOrderNotFound = false;
        boolean threwOrderNotUpdateable = false;
        boolean threwUserMismatch = false;

        NewOrder updates = new NewOrder();
        
        try {
            System.out.println(orderRepo.findById(1).get().getOrderStatus());
            service.updateOrder(testUser.getId(), 1, updates);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (OrderNotUpdateableException e) {
            threwOrderNotUpdateable = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }

        assertFalse(threwOrderNotFound);
        assertTrue(threwOrderNotUpdateable);
        assertFalse(threwUserMismatch);

    }

    @Test
    @DirtiesContext
    public void doesUpdateOrderThrowUserMismatch() {

        User testUser = userRepo.findTopByUserName("testDriver").get();
        boolean threwOrderNotFound = false;
        boolean threwOrderNotUpdateable = false;
        boolean threwUserMismatch = false;

        Order orderToUpdate = orderRepo.findById(1).get();
        orderToUpdate.setOrderStatus("placed");
        orderRepo.save(orderToUpdate);

        NewOrder updates = new NewOrder();
        
        try {
            service.updateOrder(testUser.getId(), 1, updates);
        } catch (OrderNotFoundException e) {
            threwOrderNotFound = true;
        } catch (OrderNotUpdateableException e) {
            threwOrderNotUpdateable = true;
        } catch (UserMismatchException e) {
            threwUserMismatch = true;
        }

        assertFalse(threwOrderNotFound);
        assertFalse(threwOrderNotUpdateable);
        assertTrue(threwUserMismatch);

    }


}