package com.smoothstack.ordermicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.ordermicroservice.data.OrderInformation;

import org.hibernate.MappingException;
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

    @Autowired
    UserRepository userRepo;
    /**
     * Finds a single order of the given user.
     * 
     * @param userId
     * @param orderId
     * @return
     */
    public ResponseEntity<OrderInformation> getOrderDetails(Integer userId, Integer orderId) {

        OrderInformation info = new OrderInformation();
        
        System.out.println("In get order details");

        try {
            List<Order> orders = getUserOrders(userId);
            if (orders != null && orders.size() > 0) {
                for (Order o: orders) {
                    if (o.getId() == orderId) {
                        info = processOrder(o);
                        return ResponseEntity.status(HttpStatus.OK).body(info);
                    }
                }
            }
        } catch (MappingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Retrieves the entire order history of a given user.
     * 
     * @param userId The id of the user whos orders are to be retrieved.
     * @return A list of OrderInformation objects, wrapped in a Response entity.
     */
    public ResponseEntity<List<OrderInformation>> getOrderHistory(Integer userId) {
        List<OrderInformation> processedOrders = new ArrayList<OrderInformation>();
        try {
            List<Order> orders = getUserOrders(userId);
            if (orders != null && orders.size() > 0) {
                for (Order o: orders) {
                    processedOrders.add(processOrder(o));
                }
                return ResponseEntity.status(HttpStatus.OK).body(processedOrders);
            }
        } catch (MappingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Finds all of a given users orders.
     * 
     * @param userId The id of the user whos orders are being found.
     * @return A list of Order objects representing all of the given users orders.
     */
    private List<Order> getUserOrders(Integer userId) {
        User user = new User();
        List<Order> orders = new ArrayList<Order>();
        try {
            user = userRepo.getById(userId);
            orders = orderRepo.findAllByCustomer(user);
        } catch (MappingException e) {
            return null;
        }
        return orders;
    }

    /**
     * Processes an order object into an OrderInformation Object without sensitive information.
     * 
     * @param order The order to process.
     * @return The processed order as an Order Information object.
     */
    private OrderInformation processOrder(Order order) {
        OrderInformation info = OrderInformation.builder()
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
        return info;
    }   
}
