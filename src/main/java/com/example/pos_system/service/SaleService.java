package com.example.pos_system.service;

import com.example.pos_system.entity.Sale;
import com.example.pos_system.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository repository;

    public SaleService(SaleRepository repository) {
        this.repository = repository;
    }

    public List<Sale> getAllSales() {
        return repository.findAll();
    }

    public Sale getSaleById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Sale saveSale(Sale sale) {
        sale.setSaleDate(LocalDateTime.now());
        return repository.save(sale);
    }

    public void deleteSale(Long id) {
        repository.deleteById(id);
    }
}