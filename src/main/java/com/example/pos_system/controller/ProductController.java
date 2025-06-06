package com.example.pos_system.controller;

import com.example.pos_system.entity.Product;
import com.example.pos_system.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.getProductById(id)
                .map(product -> {
                    product.setProductName(productDetails.getProductName());
                    product.setPrice(productDetails.getPrice());
                    product.setDescription(productDetails.getDescription());
                    product.setUser(productDetails.getUser());
                    product.setCategory(productDetails.getCategory());
                    Product updatedProduct = productService.saveProduct(product);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
