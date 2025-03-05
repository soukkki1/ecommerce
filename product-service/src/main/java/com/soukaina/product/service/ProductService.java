package com.soukaina.product.service;

import com.soukaina.product.model.Product;
import com.soukaina.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    public Product getProductById(UUID id) {
        System.out.println("Fetching from DB: Product ID " + id);
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Cacheable(value = "allProducts")
    public Page<Product> getAllProducts(Pageable pageable) {
        System.out.println("Fetching all products from DB");
        return productRepository.findAll(pageable);
    }


    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(UUID id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setCategory(updatedProduct.getCategory());

        return productRepository.save(existingProduct);
    }

    @Cacheable(value = "searchedProducts", key = "#keyword")
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }


    public void deleteProductById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
    @CacheEvict(value = "allProducts", allEntries = true)
    public void clearCache() {
        System.out.println("Cache cleared!");
    }

    public void applyDiscount(UUID productId, double discountPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setDiscountPrice(discountPrice);
        product.setOnSale(true);
        productRepository.save(product);
    }


    public String saveProductImage(MultipartFile file) throws IOException {
        Path filePath = uploadDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(filePath);
        return filePath.toString();
    }
}
