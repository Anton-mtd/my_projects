package org.skomorokhin.marketautumn.repositories.specification;

import org.skomorokhin.marketautumn.model.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> priceGreaterOrEqualsThan(Integer minPrice) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));

    }

    public static Specification<Product> priceLessOrEqualsThan(Integer maxPrice) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));

    }
}
