package org.skomorokhin.marketautumn.repositories;

import org.skomorokhin.marketautumn.model.entities.Cart;
import org.skomorokhin.marketautumn.model.entities.CartProducts;
import org.skomorokhin.marketautumn.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface CartProductsRepository extends JpaRepository<CartProducts, Integer> {


    List<CartProducts> findAllByCartId(Integer cartId);

    void deleteById(Integer cartProductId);

    @Transactional
    @Modifying
    @Query("delete from CartProducts cp where cp.cart.id=:cartId")
    void deleteAllByCartId(Integer cartId);

    @Query("select cp from CartProducts cp where cp.cart.id=:cartId AND cp.product.id=:productId")
    CartProducts findByCartIdAndProductId(Integer cartId, Integer productId);

}
