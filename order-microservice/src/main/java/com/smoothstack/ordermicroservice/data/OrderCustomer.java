package com.smoothstack.ordermicroservice.data;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomer {    
    
    private Long customer_id;

    private Long order_id;
}
