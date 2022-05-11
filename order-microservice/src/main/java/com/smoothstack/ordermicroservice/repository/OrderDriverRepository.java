package com.smoothstack.ordermicroservice.repository;

import java.util.Optional;

import com.smoothstack.ordermicroservice.data.OrderDriver;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDriverRepository extends CrudRepository<OrderDriver, Long> {
    Optional<OrderDriver> findByOrder_id(Long order_id);
}