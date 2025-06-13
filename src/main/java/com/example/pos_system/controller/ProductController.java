package com.example.pos_system.controller;

import com.example.pos_system.entity.Product;
import com.example.pos_system.entity.User;
import com.example.pos_system.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new product
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Product created successfully",
                "data", savedProduct
        ));
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.getProductById(id).map(product -> {
            product.setProductName(productDetails.getProductName());
            product.setPrice(productDetails.getPrice());
            product.setDescription(productDetails.getDescription());
            product.setUser(productDetails.getUser());
            product.setCategory(productDetails.getCategory());

            Product updatedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(Map.of(
                    "message", "Product updated successfully",
                    "data", updatedProduct
            ));
        }).orElse(ResponseEntity.status(404).body(Map.of(
                "error", "Product not found"
        )));
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productService.getProductById(id).map(product -> {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Product deleted successfully"
            ));
        }).orElse(ResponseEntity.status(404).body(Map.of(
                "error", "Product not found"
        )));
    }


}
