package org.skomorokhin.marketautumn.services;

import lombok.AllArgsConstructor;
import org.skomorokhin.marketautumn.converters.ProductConverter;
import org.skomorokhin.marketautumn.dto.CartProductsDto;
import org.skomorokhin.marketautumn.dto.ProductDto;
import org.skomorokhin.marketautumn.model.entities.Cart;
import org.skomorokhin.marketautumn.model.entities.CartProducts;
import org.skomorokhin.marketautumn.model.entities.Product;
import org.skomorokhin.marketautumn.repositories.CartProductsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartProductsService {

    private CartProductsRepository cartProductsRepository;


    public void saveCartProducts(CartProducts cartProducts) {
        CartProducts cp = findByCartIdAndProductId(cartProducts);
        if (cp != null) {
            cp.setQuantity(cp.getQuantity() + 1);
            cartProductsRepository.saveAndFlush(cp);
        } else {
            cartProductsRepository.saveAndFlush(cartProducts);
        }
    }

    public List<CartProductsDto> getCartProductsByCartId(Integer cartId) {
        List<CartProductsDto> cpDto = new ArrayList<>();

        cartProductsRepository.findAllByCartId(cartId).forEach(cp -> cpDto.add(
                CartProductsDto.builder()
                        .id(cp.getId())
                        .cartId(cp.getCart().getId())
                        .productDto(ProductConverter.productToDto(cp.getProduct()))
                        .quantity(cp.getQuantity())
                        .build()
        ));
        return cpDto;
    }


    public void deleteByCartProductId(Integer cartProductId) {
        cartProductsRepository.deleteById(cartProductId);
    }

    public void deleteAllByCartId(Integer cartId) {
        cartProductsRepository.deleteAllByCartId(cartId);
    }


    private CartProducts findByCartIdAndProductId(CartProducts cartProducts) {
        Integer cartId = cartProducts.getCart().getId();
        Integer productId = cartProducts.getProduct().getId();
        return cartProductsRepository.findByCartIdAndProductId(cartId, productId);
    }

}
