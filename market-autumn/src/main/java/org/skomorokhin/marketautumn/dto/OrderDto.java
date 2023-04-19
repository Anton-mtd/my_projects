package org.skomorokhin.marketautumn.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDto {

    private Integer id;

    private Integer price;

    private LocalDateTime date;

}
