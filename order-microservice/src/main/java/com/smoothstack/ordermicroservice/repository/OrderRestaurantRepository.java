package com.smoothstack.ordermicroservice.repository;

import java.util.List;
import java.util.Optional;

import com.smoothstack.ordermicroservice.data.OrderRestaurant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRestaurantRepository extends CrudRepository<OrderRestaurant, Long> {
    Optional<List<OrderRestaurant>> findAllByOrder_id(Long order_id);
}