package com.example.pos_system.controller;

import com.example.pos_system.entity.Sale;
import com.example.pos_system.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sale> getAll() {
        return service.getAllSales();
    }

    @GetMapping("/{id}")
    public Sale getById(@PathVariable Long id) {
        return service.getSaleById(id);
    }

    @PostMapping
    public ResponseEntity<Sale> create(@RequestBody Sale sale) {
        Sale saved = service.saveSale(sale);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteSale(id);
    }
}
