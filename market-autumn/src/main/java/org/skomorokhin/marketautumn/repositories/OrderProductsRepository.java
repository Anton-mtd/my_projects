package org.skomorokhin.marketautumn.repositories;

import org.skomorokhin.marketautumn.model.entities.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Integer> {

    List<OrderProducts> findAllByOrderId(Integer orderId);
}
