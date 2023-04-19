package org.skomorokhin.marketautumn.controllers;

import lombok.RequiredArgsConstructor;
import org.skomorokhin.marketautumn.dto.ProductDto;
import org.skomorokhin.marketautumn.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/showAll")
    public Page<ProductDto> getAllProducts(@RequestParam (name = "page", defaultValue = "1") Integer page,
                                           @RequestParam(name = "min_price", required = false) Integer minPrice,
                                           @RequestParam(name = "max_price", required = false) Integer maxPrice
    ) {
        if (page < 1) {
            page = 1;
        }
        return productService.find(page, minPrice, maxPrice);
    }


    @PostMapping ("/product")
    public void add(@RequestBody ProductDto productDto) {
        productService.add(productDto);
    }

    @PutMapping("/product")
    public void update(@RequestBody ProductDto productDto) {
        productService.update(productDto);
    }

    @DeleteMapping("/product")
    public void deleteProductById(@RequestParam Integer id) {
        productService.deleteById(id);
    }
}
