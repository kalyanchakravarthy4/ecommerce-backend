package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ---------- PUBLIC GET ----------
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ---------- ADMIN: CREATE PRODUCT ----------
    @PostMapping("/admin/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        // basic null checks – you can remove if you want zero validation
        if (product.getName() == null || product.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (product.getPrice() == null) {
            return ResponseEntity.badRequest().build();
        }

        // ensure id is null so Mongo creates a new one
        product.setId(null);

        // convert price to BigDecimal if needed
        if (!(product.getPrice() instanceof BigDecimal)) {
            product.setPrice(new BigDecimal(product.getPrice().toString()));
        }

        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------- ADMIN: DELETE PRODUCT ----------
    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
