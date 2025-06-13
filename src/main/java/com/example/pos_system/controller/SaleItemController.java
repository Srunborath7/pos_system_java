package com.example.pos_system.controller;

import com.example.pos_system.entity.SaleItem;
import com.example.pos_system.service.SaleItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/sale-items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @GetMapping
    public ResponseEntity<?> getAllSaleItems() {
        List<SaleItem> saleItems = saleItemService.getAllSaleItems(); // Implement in service if needed
        return ResponseEntity.ok(Map.of("message", "Sale items retrieved successfully", "data", saleItems));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleItemById(@PathVariable Long id) {
        return saleItemService.getSaleItemById(id) // Implement Optional<SaleItem> method in service
                .map(item -> ResponseEntity.ok(Map.of("message", "Sale item found", "data", item)))
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Sale item not found", "id", id)));
    }
    @PostMapping("/bulk")
    public ResponseEntity<?> createSaleItems(@RequestBody List<SaleItem> saleItems) {
        try {
            List<SaleItem> savedItems = new ArrayList<>();
            for (SaleItem item : saleItems) {
                savedItems.add(saleItemService.saveSaleItem(item));
            }
            return ResponseEntity.status(201).body(
                    Map.of("message", "Sale items created successfully", "data", savedItems)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Error creating sale items", "error", e.getMessage())
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createSaleItem(@RequestBody SaleItem saleItem) {
        try {
            SaleItem saved = saleItemService.saveSaleItem(saleItem);
            return ResponseEntity.status(201).body(
                    Map.of("message", "Sale item created successfully", "data", saved)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Error creating sale item", "error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSaleItem(@PathVariable Long id, @RequestBody SaleItem saleItem) {
        try {
            SaleItem updated = saleItemService.updateSaleItem(id, saleItem);
            return ResponseEntity.ok(
                    Map.of("message", "Sale item updated successfully", "data", updated)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Error updating sale item", "error", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSaleItem(@PathVariable Long id) {
        try {
            saleItemService.deleteSaleItem(id);
            return ResponseEntity.ok(
                    Map.of("message", "Sale item deleted successfully", "id", id)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Error deleting sale item", "error", e.getMessage())
            );
        }
    }
}
