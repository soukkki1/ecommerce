package com.soukaina.product.controller;

import com.soukaina.product.model.Product;
import com.soukaina.product.repository.ProductRepository;
import com.soukaina.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/{productId}/upload")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        try {
            String filePath = productService.saveProductImage(file);
            return ResponseEntity.ok("File uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File upload failed");
        }
    }

}
