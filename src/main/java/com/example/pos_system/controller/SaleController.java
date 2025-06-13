package com.example.pos_system.controller;

import com.example.pos_system.entity.Sale;
import com.example.pos_system.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Sale created successfully",
                    "data", savedSale
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error saving sale: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Long id, @RequestBody Sale saleDetails) {
        return saleService.getSaleById(id).map(existingSale -> {
            existingSale.setDescription(saleDetails.getDescription());
            existingSale.setCustomer(saleDetails.getCustomer());
            existingSale.setUser(saleDetails.getUser());
            if (saleDetails.getSaleDate() != null) {
                existingSale.setSaleDate(saleDetails.getSaleDate());
            }

            Sale updatedSale = saleService.saveSale(existingSale);
            return ResponseEntity.ok(Map.of(
                    "message", "Sale updated successfully",
                    "data", updatedSale
            ));
        }).orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                "error", "Sale not found"
        )));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable Long id) {
        return saleService.getSaleById(id).map(sale -> {
            saleService.deleteSale(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Sale deleted successfully"
            ));
        }).orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                "error", "Sale not found"
        )));
    }

}
