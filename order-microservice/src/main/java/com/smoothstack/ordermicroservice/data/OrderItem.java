package com.smoothstack.ordermicroservice.data;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    
    private Long order_id;

    private Long menu_items_id;

    private String notes;

    private Double discount;

    private Double price;
}
