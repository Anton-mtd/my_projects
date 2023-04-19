package org.skomorokhin.marketautumn.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderProductsDto {

    private Integer id;

    private Integer orderId;

    private ProductDto productDto;

    private Integer quantity;
}
