package com.soukaina.product.service;

import com.soukaina.product.model.Product;
import com.soukaina.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "products", key = "#id")  // Caches product by ID
    public Product getProductById(Long id) {
        System.out.println("Fetching from DB: Product ID " + id);
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Cacheable(value = "allProducts")  // Caches all products
    public List<Product> getAllProducts() {
        System.out.println("Fetching all products from DB");
        return productRepository.findAll();
    }

    @CacheEvict(value = "allProducts", allEntries = true)
    public void clearCache() {
        System.out.println("Cache cleared!");
    }
}
