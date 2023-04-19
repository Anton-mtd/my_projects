package org.skomorokhin.marketautumn.validators;

import org.skomorokhin.marketautumn.dto.ProductDto;
import org.skomorokhin.marketautumn.exceptions.ValidateException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ProductValidator {

    public void validate(ProductDto productDto) {
        List <String> errors = new ArrayList<>();

        if (productDto.getPrice() < 1) {
            errors.add("Цена товара не может быть меньше одного");
        }
        if (productDto.getTitle().isBlank()) {
            errors.add("Название товара не может быть пустым");
        }

        if (!errors.isEmpty()) {
            throw new ValidateException(errors);
        }
    }
}
