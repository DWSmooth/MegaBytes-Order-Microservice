package com.smoothstack.ordermicroservice.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontEndOrderItem {
    
    private Integer id;

    private String name;

    private String description;

    private String notes;

    private Double discount;

    private Double price;

    private Boolean enabled;

}
