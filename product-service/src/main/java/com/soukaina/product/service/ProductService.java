package com.soukaina.product.service;

import com.soukaina.product.model.Category;
import com.soukaina.product.model.Product;
import com.soukaina.product.repository.CategoryRepository;
import com.soukaina.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;
import xyz.capybara.clamav.exceptions.ClamavException;


import java.io.IOException;
import java.io.InputStream;
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
    private final CategoryRepository categoryRepository;
    private final ClamavClient clamavClient;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, @Value("${clamav.host}") String clamavHost,
                          @Value("${clamav.port}") int clamavPort) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.clamavClient = new ClamavClient(clamavHost, clamavPort);
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
        UUID categoryId = product.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);
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


    public String saveProductImage(MultipartFile file) throws IOException, ClamavException {
        Path tempFilePath = Files.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFilePath);
        try (InputStream inputStream = Files.newInputStream(tempFilePath)) {
            ScanResult scanResult = clamavClient.scan(inputStream);
            if (scanResult == null) {
                Files.delete(tempFilePath);
                throw new IOException("Virus detected! File upload rejected.");
            }
        }
        Path filePath = uploadDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(filePath);
        return filePath.toString();
    }
}
