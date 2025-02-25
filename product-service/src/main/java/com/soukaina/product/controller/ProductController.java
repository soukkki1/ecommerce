package com.soukaina.product.controller;

import com.soukaina.product.model.Product;
import com.soukaina.product.repository.ProductRepository;
import com.soukaina.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.capybara.clamav.exceptions.ClamavException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    // CRUD operations for Product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @PostMapping("/{productId}/discount")
    public ResponseEntity<String> applyDiscount(@PathVariable UUID productId, @RequestParam double discountPrice) {
        productService.applyDiscount(productId, discountPrice);
        return ResponseEntity.ok("Discount applied successfully!");
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @GetMapping("/clear-cache")
    public String clearCache() {
        productService.clearCache();
        return "Cache cleared!";
    }

    @PostMapping("/{productId}/upload")
    public ResponseEntity<String> uploadProductImage(@PathVariable UUID productId, @RequestParam("file") MultipartFile file) {
        try {
            String filePath = productService.saveProductImage(file);
            return ResponseEntity.ok("File uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File upload failed");
        } catch (ClamavException e) {
            return ResponseEntity.badRequest().body("Virus detected in your file");
        }
    }

}
