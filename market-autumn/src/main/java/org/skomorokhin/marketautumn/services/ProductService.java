package org.skomorokhin.marketautumn.services;


import lombok.RequiredArgsConstructor;
import org.skomorokhin.marketautumn.converters.ProductConverter;
import org.skomorokhin.marketautumn.dto.ProductDto;
import org.skomorokhin.marketautumn.exceptions.ValidateException;
import org.skomorokhin.marketautumn.model.entities.Product;
import org.skomorokhin.marketautumn.repositories.ProductRepository;
import org.skomorokhin.marketautumn.repositories.specification.ProductSpecification;
import org.skomorokhin.marketautumn.validators.ProductValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public Page<ProductDto> find(Integer p, Integer minPrice, Integer maxPrice) {
        Specification<Product> spec = Specification.where(null);
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceGreaterOrEqualsThan(minPrice));
        } if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceLessOrEqualsThan(maxPrice));
        }

        return productRepository.findAll(spec, PageRequest.of(p -1, 10)).map(ProductConverter::productToDto);
    }

    public ProductDto findByID(Integer id) {
        return productRepository.findById(id).map(ProductConverter::productToDto).orElseThrow();
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    public ProductDto add(ProductDto productDto) {
        productValidator.validate(productDto);
        productRepository.save(ProductConverter.dtoToProduct(productDto));
        return productDto;
    }

    @Transactional
    public ProductDto update(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(
                () -> new ValidateException(List.of("Продукт с id=" + productDto.getId() + " не существует")));
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        return productDto;
    }

}
