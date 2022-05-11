package com.smoothstack.ordermicroservice.repository;

import com.smoothstack.ordermicroservice.data.Order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    
}
