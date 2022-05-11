package com.smoothstack.ordermicroservice.repository;

import java.util.Optional;

import com.smoothstack.ordermicroservice.data.OrderCustomer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCustomerRepository extends CrudRepository<OrderCustomer, Long> {
    Optional<OrderCustomer> findByOrder_id(Long order_id);
}
