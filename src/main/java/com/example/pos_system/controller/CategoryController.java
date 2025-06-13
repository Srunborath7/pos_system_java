package com.example.pos_system.controller;

import com.example.pos_system.entity.Category;
import com.example.pos_system.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> create(@RequestBody Category category) {
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Category inserted successfully",
                "data", savedCategory
        ));
    }

    // Create multiple categories
    @PostMapping("/batch")
    public ResponseEntity<?> createMultiple(@RequestBody List<Category> categories) {
        List<Category> savedCategories = categories.stream()
                .map(categoryService::save)
                .toList();

        return ResponseEntity.status(201).body(Map.of(
                "message", "Categories inserted successfully",
                "data", savedCategories
        ));
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryService.findById(id).map(category -> {
            category.setCateName(categoryDetails.getCateName());
            category.setDescription(categoryDetails.getDescription());
            category.setUser(categoryDetails.getUser());
            Category updatedCategory = categoryService.save(category);
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "data", updatedCategory
            ));
        }).orElse(ResponseEntity.status(404).body(Map.of(
                "error", "Category not found"
        )));
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return categoryService.findById(id).map(category -> {
            categoryService.deleteById(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Category deleted successfully"
            ));
        }).orElse(ResponseEntity.status(404).body(Map.of(
                "error", "Category not found"
        )));
    }

}
