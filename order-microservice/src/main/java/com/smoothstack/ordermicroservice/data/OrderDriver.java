package com.smoothstack.ordermicroservice.data;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_driver")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDriver {
    
    private Long driver_id;

    private Long order_id;
}
