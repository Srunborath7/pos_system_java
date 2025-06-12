package com.example.pos_system.service;

import com.example.pos_system.entity.SaleItem;
import com.example.pos_system.repository.SaleItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final SaleItemRepository saleItemRepository;

    public InvoiceService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public List<SaleItem> getItemsBySaleId(Long saleId) {
        return saleItemRepository.findBySale_SaleId(saleId);
    }
}
