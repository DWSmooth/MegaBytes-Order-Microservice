package com.smoothstack.ordermicroservice.repository;

import java.util.List;
import java.util.Optional;

import com.smoothstack.ordermicroservice.data.OrderItem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    Optional<List<OrderItem>> findAllByOrder_id(Long order_id);
}