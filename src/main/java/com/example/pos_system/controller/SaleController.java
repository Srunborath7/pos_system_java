package com.example.pos_system.controller;

import com.example.pos_system.entity.Sale;
import com.example.pos_system.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createSale(@RequestBody Sale sale) {
        try {
            Sale savedSale = saleService.saveSale(sale);
            return ResponseEntity.status(201).body(savedSale);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving sale: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Long id, @RequestBody Sale saleDetails) {
        return saleService.getSaleById(id).map(existingSale -> {
            // Update fields (only description, customer, user for example)
            existingSale.setDescription(saleDetails.getDescription());
            existingSale.setCustomer(saleDetails.getCustomer());
            existingSale.setUser(saleDetails.getUser());
            // If you want to allow updating saleDate:
            if (saleDetails.getSaleDate() != null) {
                existingSale.setSaleDate(saleDetails.getSaleDate());
            }

            Sale updatedSale = saleService.saveSale(existingSale);
            return ResponseEntity.ok(updatedSale);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable Long id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting sale: " + e.getMessage());
        }
    }
}
