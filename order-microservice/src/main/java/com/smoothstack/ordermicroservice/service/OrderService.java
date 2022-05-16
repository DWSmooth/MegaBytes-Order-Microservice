package com.smoothstack.ordermicroservice.service;

import java.util.ArrayList;
import java.util.List;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.ordermicroservice.data.OrderInformation;

import org.springframework.beans.factory.annotation.Autowired;
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
     * Finds a single order by order ID.
     * 
     * @param userId
     * @param orderId
     * @return
     */
    public OrderInformation getOrderDetails(Integer userId, Integer orderId) {
        try {
            Order order  = orderRepo.getById(orderId);
            if (order.getCustomer().getId() != userId) {
                //TODO: actually throw an error here
                return null;
            }
            return OrderInformation.getFrontendData(order);
        } catch (Exception e) {
            //TODO: actually throw an error here
            return null;
        }
    }

    /**
     * Retrieves the entire order history of a given user.
     * 
     * @param userId The id of the user whos orders are to be retrieved.
     * @return A list of OrderInformation objects, wrapped in a Response entity.
     */
    public List<OrderInformation> getOrderHistory(Integer userId) {
        try {
            List<OrderInformation> processedOrders = new ArrayList<>();
            List<Order> orders = getUserOrders(userId);
            if (orders != null && orders.size() > 0) {
                for (Order o: orders) {
                    processedOrders.add(OrderInformation.getFrontendData(o));
                }
                return processedOrders;
            }
            //TODO: actually throw an error here
            return null;
        } catch (Exception e) {
            //TODO: actually throw an error here
            return null;
        }
    }

    /**
     * Finds all of a given users orders.
     * 
     * @param userId The id of the user whos orders are being found.
     * @return A list of Order objects representing all of the given users orders.
     */
    private List<Order> getUserOrders(Integer userId) {
        User user;
        List<Order> orders;
        try {
            user = userRepo.getById(userId);
            orders = orderRepo.findAllByCustomer(user);
        } catch (Exception e) {
            return null;
        }
        return orders;
    }
}
