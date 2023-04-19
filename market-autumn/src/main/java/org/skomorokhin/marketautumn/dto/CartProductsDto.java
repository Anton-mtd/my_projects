package org.skomorokhin.marketautumn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartProductsDto {

    private Integer id;

    private Integer cartId;

    private ProductDto productDto;

    private Integer quantity;
}
