package com.smoothstack.ordermicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.ordermicroservice.data.OrderInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderItemRepository itemRepo;
    
    public ResponseEntity<OrderInformation> getOrderDetails(Integer orderId) {

        OrderInformation info = new OrderInformation();
        
        System.out.println("In get order details");

        try {
            Order order = orderRepo.getById(orderId);
            info = OrderInformation.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .restaurantNotes(order.getRestaurantNotes())
                .driverNotes(order.getDriverNotes())
                .subTotal(order.getSubTotal())
                .deliveryFee(order.getDeliveryFee())
                .tax(order.getTax())
                .tip(order.getTip())
                .total(order.getTotal())
                .timeCreated(order.getTimeCreated())
                .scheduledFor(order.getScheduledFor())
                .netLoyalty(order.getNetLoyalty())
                .driverFirstName(
                    order.getDriver()
                    .getUserInformation()
                    .getFirstName()
                )
                .restaurantNames(
                    order.getRestaurants()
                    .stream()
                    .map(r -> r.getName())
                    .collect(Collectors.toList())
                )
                .discounts(order.getDiscounts())
                .items(order.getOrderItems())
                .build();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }

    public ResponseEntity<List<OrderInformation>> getOrderHistory() {
        List<OrderInformation> processedOrders = new ArrayList();
        try {
            List<Order> orders = orderRepo.findAllByCustomer();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
