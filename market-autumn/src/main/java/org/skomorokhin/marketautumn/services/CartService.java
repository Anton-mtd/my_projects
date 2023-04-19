package org.skomorokhin.marketautumn.services;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.skomorokhin.marketautumn.model.entities.Cart;
import org.skomorokhin.marketautumn.model.entities.User;
import org.skomorokhin.marketautumn.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {

    CartRepository cartRepository;

    public Optional<Cart> getCartByUser(User user) {
        Optional<Cart> cart = cartRepository.findByUserId(user.getId());
        if (!cart.isPresent()) {
            cart = Optional.of(Cart.builder()
                    .user(user)
                    .build());
            cartRepository.saveAndFlush(cart.get());
        }
        return cart;
    }

}
