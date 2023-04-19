package org.skomorokhin.marketautumn.controllers;

import lombok.RequiredArgsConstructor;
import org.skomorokhin.marketautumn.dto.CartProductsDto;
import org.skomorokhin.marketautumn.dto.OrderDto;
import org.skomorokhin.marketautumn.dto.OrderProductsDto;
import org.skomorokhin.marketautumn.dto.ProductDto;
import org.skomorokhin.marketautumn.model.entities.*;
import org.skomorokhin.marketautumn.services.CartProductsService;
import org.skomorokhin.marketautumn.services.CartService;
import org.skomorokhin.marketautumn.services.OrderService;
import org.skomorokhin.marketautumn.services.UserService;
import org.skomorokhin.marketautumn.utils.JwtTokenUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final CartProductsService cartProductsService;
    private final OrderService orderService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/cart/putToCart")
    public void putToCart(@RequestHeader("Authorization") String token, Product product){
            User user = getCurrentUser(token);

            Cart cart = cartService.getCartByUser(user).orElseThrow();
            cartProductsService.saveCartProducts(CartProducts.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build());
    }


    @GetMapping("/cart/showCart")
    public List<CartProductsDto> showCart(@RequestHeader("Authorization") String token)
    {
        return cartProductsService.getCartProductsByCartId(getCurrentUser(token).getCart().getId());
    }


    @DeleteMapping ("/cart/delFromCart")
    public void delFromCart(@RequestHeader("Authorization") String token, Integer cartProductId) {
        cartProductsService.deleteByCartProductId(cartProductId);

    }

    @PostMapping("/cart/createOrder")
    public void createOrder(@RequestHeader("Authorization") String token) {
        orderService.createOrder(getCurrentUser(token), showCart(token));
        cartProductsService.deleteAllByCartId(getCurrentUser(token).getCart().getId());
    }

    @GetMapping("/cart/showOrders")
    public List<OrderDto> showOrders(@RequestHeader("Authorization") String token) {
        return orderService.getOrdersByUserId(getCurrentUser(token).getId());
    }

    @GetMapping("/cart/orderInfo")
    public List<OrderProductsDto> orderInfo(Integer orderId) {
        return orderService.getOrderProductsByOrderId(orderId);
    }

    private User getCurrentUser(String token) {
        return userService
                .getUserByUsername(jwtTokenUtil.getUsernameFromToken(token.substring(7)))
                .get();
    }
}
