package com.soukaina.product.service;

import com.soukaina.product.model.Product;
import com.soukaina.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final Path uploadDir = Paths.get("uploads");


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!", e);
        }
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("Fetching from DB: Product ID " + id);
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Cacheable(value = "allProducts")
    public List<Product> getAllProducts() {
        System.out.println("Fetching all products from DB");
        return productRepository.findAll();
    }

    @CacheEvict(value = "allProducts", allEntries = true)
    public void clearCache() {
        System.out.println("Cache cleared!");
    }

    public String saveProductImage(MultipartFile file) throws IOException {
        Path filePath = uploadDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(filePath);
        return filePath.toString();
    }
}
