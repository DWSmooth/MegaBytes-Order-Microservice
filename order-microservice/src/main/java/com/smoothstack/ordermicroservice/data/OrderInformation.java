package com.smoothstack.ordermicroservice.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.common.models.Discount;
import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInformation {

    private Integer orderId;

    private String orderStatus;

    private String restaurantNotes;

    private String driverNotes;

    private Double subTotal;

    private Double deliveryFee;

    private Double tax;

    private Double tip;

    private Double total;

    private LocalDateTime timeCreated;

    private LocalDateTime scheduledFor;

    private Integer netLoyalty;

    private String driverFirstName;

    private List<String> restaurantNames;

    private List<Discount> discounts;

    private List<OrderItem> items;

    /**
     * Processes an order object into an OrderInformation Object without sensitive information.
     * 
     * @param order The order to process.
     * @return The processed order as an Order Information object.
     */
    public static OrderInformation getFrontendData(Order order) {
        return OrderInformation.builder()
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
    }
    
}
