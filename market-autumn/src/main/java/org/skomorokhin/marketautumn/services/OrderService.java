package org.skomorokhin.marketautumn.services;


import lombok.RequiredArgsConstructor;
import org.skomorokhin.marketautumn.converters.ProductConverter;
import org.skomorokhin.marketautumn.dto.CartProductsDto;
import org.skomorokhin.marketautumn.dto.OrderDto;
import org.skomorokhin.marketautumn.dto.OrderProductsDto;
import org.skomorokhin.marketautumn.model.entities.Order;
import org.skomorokhin.marketautumn.model.entities.OrderProducts;
import org.skomorokhin.marketautumn.model.entities.User;
import org.skomorokhin.marketautumn.repositories.OrderProductsRepository;
import org.skomorokhin.marketautumn.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderProductsRepository orderProductsRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public void createOrder(User currentUser, List<CartProductsDto> productsInCart) {
        Order order = saveOrder(currentUser, productsInCart);
        productsInCart.forEach(pc ->
                orderProductsRepository.saveAndFlush(
                        OrderProducts.builder()
                                .order(order)
                                .product(ProductConverter.dtoToProduct(pc.getProductDto()))
                                .quantity(pc.getQuantity())
                                .build()
                )
        );
    }

    public List<OrderDto> getOrdersByUserId(Integer userId) {
        List<OrderDto> orders = new ArrayList<>();
        orderRepository.findAllByUserId(userId).forEach(o -> orders.add(
                OrderDto.builder()
                        .id(o.getId())
                        .date(o.getDate())
                        .price(o.getPrice())
                        .build()
        ));
        return orders;
    }


    public List<OrderProductsDto> getOrderProductsByOrderId(Integer orderId) {
        List<OrderProductsDto> orderProducts = new ArrayList<>();
        AtomicInteger id = new AtomicInteger();
        orderProductsRepository.findAllByOrderId(orderId).forEach(op -> orderProducts.add(
                OrderProductsDto.builder()
                        .id(id.incrementAndGet())
                        .orderId(op.getOrder().getId())
                        .productDto(ProductConverter.productToDto(op.getProduct()))
                        .quantity(op.getQuantity())
                        .build()
                )
        );
        return orderProducts;
    }

    private Integer getOrderNumber() {
        Integer numOrder = 1;
        if (orderRepository.findMaxId() != null) {
            numOrder = orderRepository.findMaxId() + 1;
        }
        return numOrder;
    }


    private Integer getOrderTotalPrice(List<CartProductsDto> productsInCart) {
        Integer totalPrice = 0;
        for (CartProductsDto cp: productsInCart) {
            totalPrice += cp.getProductDto().getPrice() * cp.getQuantity();
        }
        return  totalPrice;
    }


    private Order saveOrder(User currentUser, List<CartProductsDto> productsInCart) {
        LocalDateTime now = LocalDateTime.now();
        Order order = Order.builder()
                .id(getOrderNumber())
                .user(currentUser)
                .date(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute()))
                .price(getOrderTotalPrice(productsInCart))
                .build();
        orderRepository.saveAndFlush(order);
        return order;
    }
}
