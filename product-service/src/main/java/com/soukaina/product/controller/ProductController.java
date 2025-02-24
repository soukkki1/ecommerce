package com.soukaina.product.controller;

import com.soukaina.product.model.Product;
import com.soukaina.product.repository.ProductRepository;
import com.soukaina.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ecommerce/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> listOfProducts() {
        List<Product> products = new ArrayList<Product>();
        products = productService.getAllProducts();
        return products;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/clear-cache")
    public String clearCache() {
        productService.clearCache();
        return "Cache cleared!";
    }

}
