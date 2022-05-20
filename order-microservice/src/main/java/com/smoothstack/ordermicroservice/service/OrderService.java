package com.smoothstack.ordermicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.smoothstack.common.models.Discount;
import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.OrderItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.User;
import com.smoothstack.common.repositories.DiscountRepository;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.repositories.OrderItemRepository;
import com.smoothstack.common.repositories.OrderRepository;
import com.smoothstack.common.repositories.RestaurantRepository;
import com.smoothstack.common.repositories.UserRepository;
import com.smoothstack.ordermicroservice.data.FrontEndOrderItem;
import com.smoothstack.ordermicroservice.data.NewOrder;
import com.smoothstack.ordermicroservice.data.OrderInformation;
import com.smoothstack.ordermicroservice.exceptions.OrderNotCancelableException;
import com.smoothstack.ordermicroservice.exceptions.OrderNotFoundException;
import com.smoothstack.ordermicroservice.exceptions.OrderNotUpdateableException;
import com.smoothstack.ordermicroservice.exceptions.UserMismatchException;
import com.smoothstack.ordermicroservice.exceptions.UserNotFoundException;

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

    @Autowired
    RestaurantRepository restaurantRepo;

    @Autowired
    DiscountRepository discountRepo;

    @Autowired
    MenuItemRepository menuItemRepo;
    
    /**
     * Finds a single order by order ID.
     * 
     * @param userId
     * @param orderId
     * @return
     */
    @Transactional
    public OrderInformation getOrderDetails(Integer userId, Integer orderId) throws OrderNotFoundException, UserMismatchException {
        Optional<Order> order  = orderRepo.findById(orderId);
        if (order.isPresent()) {
            if (order.get().getCustomer().getId() != userId) {
                throw new UserMismatchException("User id provided does not match order requested.");
            }
            return createFrontEndData(order.get().getId());
        }
        throw new OrderNotFoundException("No order with ID: " + orderId + " exists.");
    }

    /**
     * Retrieves the entire order history of a given user.
     * 
     * @param userId The id of the user whos orders are to be retrieved.
     * @return A list of OrderInformation objects, wrapped in a Response entity.
     */
    @Transactional
    public List<OrderInformation> getOrderHistory(Integer userId) throws UserNotFoundException {
        List<OrderInformation> processedOrders = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        try {
            orders = getUserOrders(userId);
        } catch (UserNotFoundException e) {
            throw e;
        }
        
        if (orders != null && orders.size() > 0) {
            for (Order o: orders) {
                processedOrders.add(createFrontEndData(o.getId()));
            }
        }

        return processedOrders;
    }

    /**
     * Updates an order.
     * 
     * @param userId The id of the user whos order is to be updated.
     * @param orderId The id of the order to be updated.
     * @param orderUpdates The updates to be made.
     * @return The front end info for the updated Order.
     */
    @Transactional
    public OrderInformation updateOrder(Integer userId, Integer orderId, NewOrder orderUpdates) 
    throws OrderNotFoundException, OrderNotUpdateableException, UserMismatchException {
        Optional<Order> orderToUpdate = orderRepo.findById(orderId);
        if (orderToUpdate.isPresent()) {
            if(orderToUpdate.get().getOrderStatus() == "placed") {
                if (orderToUpdate.get().getCustomer().getId() == userId) {
                    Order updatedOrder = applyDataToOrder(orderUpdates, orderToUpdate.get());
                    return createFrontEndData(orderRepo.save(updatedOrder).getId());
                }
                throw new UserMismatchException("User does not match user on order to be updated.");
            }
            throw new OrderNotUpdateableException("Order has progressed too far to update.");
        }
        throw new OrderNotFoundException("No order with ID: " + orderId + " exists to be updated.");
    }

    /**
     * Cancels an order by orderId and userId.
     * 
     * @param userId The id of the user whos order is to be canceled.
     * @param orderId The id of the order to cancel.
     * @return The canceled order.
     */
    @Transactional
    public OrderInformation cancelOrder(Integer userId, Integer orderId) throws OrderNotFoundException, OrderNotCancelableException, UserMismatchException {
        Optional<Order> orderToCancel = orderRepo.findById(orderId);
        if (orderToCancel.isPresent()) {
            if(orderToCancel.get().getOrderStatus() == "placed") {
                if(orderToCancel.get().getCustomer().getId() == userId) {
                    orderToCancel.get().setOrderStatus("canceled");
                
                    //TODO: Send confirmation to user email/phone that order has been canceled.

                    return createFrontEndData(orderRepo.save(orderToCancel.get()).getId());
                }
                throw new UserMismatchException("User does not match user on order to be canceled.");
            }
            throw new OrderNotCancelableException("Order has progressed too far to cancel.");
        }
        throw new OrderNotFoundException("No order with ID: " + orderId + " exists to be canceled.");
    }

    /**
     * Deletes order by orderId and userId.
     * 
     * @param userId The id of the user whos order is to be deleted.
     * @param orderId The id of the order to delete.
     * @return If the order was successfully canceled or not.
     */
    @Transactional
    public Boolean deleteOrder(Integer userId, Integer orderId) throws OrderNotFoundException, UserMismatchException {
        Optional<Order> orderToDelete = orderRepo.findById(orderId);
        if (orderToDelete.isPresent()) {
            if (orderToDelete.get().getCustomer().getId() != userId) {
                orderRepo.delete(orderToDelete.get());
                return true;
            }
            throw new UserMismatchException("User does not match User on order to be deleted.");
        }
        throw new OrderNotFoundException("No order with ID: " + orderId + " exists to be deleted.");
    }

    /**
     * Finds all of a given users orders.
     * 
     * @param userId The id of the user whos orders are being found.
     * @return A list of Order objects representing all of the given users orders.
     */
    @Transactional
    private List<Order> getUserOrders(Integer userId) throws UserNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            List<Order> orders = orderRepo.findAllByCustomer(user.get());
            return orders;
        }
        throw new UserNotFoundException("User with ID: " + userId + " does not exist.");
    }


    @Transactional
    private Order applyDataToOrder(NewOrder newOrder, Order orderToUpdate) {
        if(newOrder.getRestaurantNotes() != null) {
            orderToUpdate.setRestaurantNotes(newOrder.getRestaurantNotes());
        }
        if(newOrder.getDriverNotes() != null) {
            orderToUpdate.setDriverNotes(newOrder.getDriverNotes());
        }
        if(newOrder.getSubTotal() != null) {
            orderToUpdate.setSubTotal(newOrder.getSubTotal());
        }
        if(newOrder.getDeliveryFee() != null) {
            orderToUpdate.setDeliveryFee(newOrder.getDeliveryFee());
        }
        if(newOrder.getTax() != null) {
            orderToUpdate.setTax(newOrder.getTax());
        }
        if(newOrder.getTip() != null) {
            orderToUpdate.setTip(newOrder.getTip());
        }
        if(newOrder.getTotal() != null) {
            orderToUpdate.setTotal(newOrder.getTotal());;
        }
        if(newOrder.getNetLoyalty() != null) {
            orderToUpdate.setNetLoyalty(newOrder.getNetLoyalty());;
        }
        
        try {
            if(newOrder.getRestaurantIds() != null) {
                List<Restaurant> newRestaurantList = newOrder.getRestaurantIds().stream()
                .map(id -> restaurantRepo.getById(id))
                .collect(Collectors.toList());
                orderToUpdate.setRestaurants(newRestaurantList);
            }
        } catch (Exception e) {
            //TODO: Throw Restaurant not found exception
            e.printStackTrace();
        }

        try {
            if(newOrder.getDiscountIds() != null) {
                List<Discount> newDiscountsList = newOrder.getDiscountIds().stream()
                .map(id -> discountRepo.getById(id))
                .collect(Collectors.toList());
                orderToUpdate.setDiscounts(newDiscountsList);
            }
        } catch (Exception e) {
            //TODO: Throw Discount not found exception
            e.printStackTrace();
        }
        
        try {
            if(newOrder.getItems() != null) {
                List<OrderItem> newOrderItemsList = newOrder.getItems().stream()
                .map(orderItemInfo -> {
                    OrderItem item = new OrderItem();
                    item.setMenuItems(menuItemRepo.getById(orderItemInfo.getMenuItemId()));
                    item.setNotes(orderItemInfo.getNotes());
                    //TODO: actually figure out how to set discount properly
                    item.setDiscount(0.0d);
                    item.setPrice(orderItemInfo.getPrice());
                    return item;
                })
                .collect(Collectors.toList());
                orderToUpdate.setOrderItems(newOrderItemsList);
            }
        } catch (Exception e) {
            //TODO: Throw Menu-Item not found exception
            e.printStackTrace();
        }

        return orderToUpdate;
    }

    /**
     * Processes an order object into an OrderInformation Object without sensitive information.
     * 
     * @param order The order to process.
     * @return The processed order as an Order Information object.
     */
    @Transactional
    public OrderInformation createFrontEndData(Integer orderId) {

        Optional<Order> orderOptional = orderRepo.findById(orderId);
        if (!orderOptional.isPresent()) {
            System.out.println("Order not found!");
            return null;
        }
        Order order = orderOptional.get();
        OrderInformation info = new OrderInformation();

        if (order.getId() != null) {
            info.setOrderId(order.getId());
        }
        if (order.getOrderStatus() != null) {
            info.setOrderStatus(order.getOrderStatus());
        }
        if (order.getRestaurantNotes() != null) {
            info.setRestaurantNotes(order.getRestaurantNotes());
        }
        if (order.getDriverNotes() != null) {
            info.setDriverNotes(order.getDriverNotes());
        }
        if (order.getSubTotal() != null) {
            info.setSubTotal(order.getSubTotal());
        }
        if (order.getDeliveryFee() != null) {
            info.setDeliveryFee(order.getDeliveryFee());
        }
        if (order.getTax() != null) {
            info.setTax(order.getTax());
        }
        if (order.getTip() != null) {
            info.setTip(order.getTip());
        }
        if (order.getTotal() != null) {
            info.setTotal(order.getTotal());
        }
        if (order.getTimeCreated() != null) {
            info.setTimeCreated(order.getTimeCreated());
        }
        if (order.getScheduledFor() != null) {
            info.setScheduledFor(order.getScheduledFor());
        }
        if (order.getNetLoyalty() != null) {
            info.setNetLoyalty(order.getNetLoyalty());
        }
        if (order.getDriver().getUserInformation().getFirstName() != null) {
            info.setDriverFirstName(order.getDriver().getUserInformation().getFirstName());
        }
        if (order.getRestaurants() != null) {
            info.setRestaurantNames(
                order.getRestaurants().stream()
                .map(r -> r.getName())
                .collect(Collectors.toList())
            );
        }
        if (order.getDiscounts() != null) {
            info.setDiscounts(order.getDiscounts());
        }
        if (order.getOrderItems() != null) {
            System.out.println("Items size is: " + order.getOrderItems().size());
            info.setItems(
                order.getOrderItems().stream()
                .map(o -> {
                    FrontEndOrderItem item = new FrontEndOrderItem();
                    item.setId(o.getId());
                    item.setName(o.getMenuItems().getName());
                    item.setDescription(o.getMenuItems().getDescription());
                    item.setNotes(o.getNotes());
                    item.setDiscount(o.getDiscount());
                    item.setPrice(o.getPrice());
                    return item;
                })
                .collect(Collectors.toList())
            );
        }

        return info;

    }
}
