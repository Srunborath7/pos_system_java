package com.example.pos_system.controller;

import com.example.pos_system.entity.Inventory;
import com.example.pos_system.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Inventory> inventories = service.getAllInventories();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inventory list fetched successfully");
            response.put("data", inventories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Inventory inventory = service.getInventoryById(id);
            if (inventory != null) {
                return ResponseEntity.ok(Map.of("message", "Inventory fetched successfully", "data", inventory));
            }
            return ResponseEntity.status(404).body(Map.of("message", "Inventory not found with ID " + id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Inventory inventory) {
        try {
            Inventory saved = service.saveInventory(inventory);
            return ResponseEntity.status(201).body(Map.of("message", "Inventory created successfully", "data", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        try {
            Inventory updated = service.updateInventory(id, inventory);
            if (updated != null) {
                return ResponseEntity.ok(Map.of("message", "Inventory updated successfully", "data", updated));
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Inventory not found with ID " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            boolean deleted = service.deleteInventory(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Inventory deleted successfully"));
            } else {
                return ResponseEntity.status(404).body(Map.of("message", "Inventory not found with ID " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}