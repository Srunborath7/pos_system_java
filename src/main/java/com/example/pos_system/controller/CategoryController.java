package com.example.pos_system.controller;

import com.example.pos_system.entity.Category;
import com.example.pos_system.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create single category
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Category category) {
        categoryService.save(category);
        return ResponseEntity.status(201).body("Category inserted successfully");
    }

    // Create multiple categories
    @PostMapping("/batch")
    public ResponseEntity<String> createMultiple(@RequestBody List<Category> categories) {
        categories.forEach(categoryService::save);
        return ResponseEntity.status(201).body("Categories inserted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryService.findById(id).map(category -> {
            category.setCateName(categoryDetails.getCateName());
            category.setDescription(categoryDetails.getDescription());
            category.setUser(categoryDetails.getUser());
            return ResponseEntity.ok(categoryService.save(category));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
