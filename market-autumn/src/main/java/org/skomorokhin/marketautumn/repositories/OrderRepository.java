package org.skomorokhin.marketautumn.repositories;

import org.skomorokhin.marketautumn.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository <Order, Integer> {

    @Query("select max(o.id) from Order o")
    Integer findMaxId();

    List<Order> findAllByUserId(Integer userId);
}
