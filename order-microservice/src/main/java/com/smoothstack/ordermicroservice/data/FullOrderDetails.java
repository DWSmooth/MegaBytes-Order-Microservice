package com.smoothstack.ordermicroservice.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullOrderDetails {
    
    private Order order;

    private Long customerId;

    private Long driverId;

    private List<Long> restaurantIds;

    private List<Long> itemIds;

}
