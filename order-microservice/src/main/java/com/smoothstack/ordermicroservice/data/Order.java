package com.smoothstack.ordermicroservice.data;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String order_status;

    private String restaurant_notes;

    private String driver_notes;

    private Double sub_total;

    private Double delivery_fee;

    private Double tax;

    private Double tip;

    private Double total;

    private LocalDateTime time_created;

    private LocalDateTime scheduled_for;

}
