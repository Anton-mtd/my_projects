package org.skomorokhin.marketautumn.converters;


import org.skomorokhin.marketautumn.dto.ProductDto;
import org.skomorokhin.marketautumn.model.entities.Product;
import org.springframework.stereotype.Component;


public class ProductConverter {

    public static Product dtoToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .title(productDto.getTitle())
                .price(productDto.getPrice())
                .build();
    }

    public static ProductDto productToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .build();
    }

}
