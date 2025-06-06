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
    public Inventory getById(@PathVariable Long id) {
        return service.getInventoryById(id);
    }

    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody Inventory inventory) {
        Inventory saved = service.saveInventory(inventory);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public Inventory update(@PathVariable Long id, @RequestBody Inventory inventory) {
        return service.updateInventory(id, inventory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteInventory(id);
    }
}
