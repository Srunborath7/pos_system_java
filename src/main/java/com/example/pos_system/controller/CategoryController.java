package com.example.pos_system.controller;

import com.example.pos_system.entity.Category;
import com.example.pos_system.entity.User;
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

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Category category) {
        categoryService.save(category);
        return ResponseEntity.status(201).body("Category inserted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryService.findById(id).map(category -> {
            category.setCateName(categoryDetails.getCateName());
            category.setDescription(categoryDetails.getDescription());
            category.setUser(categoryDetails.getUser());
            categoryService.save(category);
            return ResponseEntity.ok("Category updated successfully");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return categoryService.findById(id).map(category -> {
            categoryService.deleteById(id);
            return ResponseEntity.ok("Category deleted successfully");
        }).orElse(ResponseEntity.notFound().build());
    }

}
