package com.smoothstack.ordermicroservice.data;

import java.time.LocalDateTime;
import java.util.List;

import com.smoothstack.common.models.Discount;
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
    
}
