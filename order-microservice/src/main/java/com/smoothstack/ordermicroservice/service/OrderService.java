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
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cancels an order by order and user ID's
     * 
     * @param userId The id of the user whos order is to be canceled.
     * @param orderId The id of the order to cancel.
     * @return The canceled order.
     */
    public OrderInformation cancelOrder(Integer userId, Integer orderId) {
        try {
            Order orderToCancel = orderRepo.getById(orderId);
            if(orderToCancel.getCustomer().getId() != userId) {
                //TODO: actually throw an error here
                return null;
            }
            orderToCancel.setOrderStatus("canceled");

            //TODO: Send confirmation to user email/phone that order has been canceled.

            return OrderInformation.getFrontendData(orderRepo.save(orderToCancel));
        } catch (Exception e) {
            //TODO: actually throw an error here
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes order by order and user ID's
     * 
     * @param userId The id of the user whos order is to be deleted.
     * @param orderId The id of the order to delete.
     * @return If the order was successfully canceled or not.
     */
    public Boolean deleteOrder(Integer userId, Integer orderId) {
        try {
            Order orderToDelete = orderRepo.getById(orderId);
            if (orderToDelete.getCustomer().getId() != userId) {
                orderRepo.delete(orderToDelete);
                return true;
            }
            return false;
        } catch (Exception e) {
            //TODO: actually throw an error here
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds all of a given users orders.
     * 
     * @param userId The id of the user whos orders are being found.
     * @return A list of Order objects representing all of the given users orders.
     */
    private List<Order> getUserOrders(Integer userId) {
        try {
            User user = userRepo.getById(userId);
            List<Order> orders = orderRepo.findAllByCustomer(user);
            return orders;
        } catch (Exception e) {
            //TODO: actually throw an error here
            e.printStackTrace();
            return null;
        }
    }
}
