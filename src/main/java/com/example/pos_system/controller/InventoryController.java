package com.example.pos_system.controller;

import com.example.pos_system.entity.Inventory;
import com.example.pos_system.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Inventory> getAll() {
        return service.getAllInventories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getById(@PathVariable Long id) {
        Inventory inventory = service.getInventoryById(id);
        if (inventory != null) {
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Inventory inventory) {
        service.saveInventory(inventory);
        return ResponseEntity.status(201).body("Inventory created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updated = service.updateInventory(id, inventory);
        if (updated != null) {
            return ResponseEntity.ok("Inventory updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteInventory(id);
        return ResponseEntity.ok("Inventory deleted successfully");
    }
}
