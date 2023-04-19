package org.skomorokhin.marketautumn.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto implements Comparable<ProductDto> {

    private Integer id;

    private String title;

    private Integer price;


    @Override
    public int compareTo(ProductDto o) {
        return this.getId().compareTo(o.id);
    }
}


