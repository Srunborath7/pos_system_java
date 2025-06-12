package com.example.pos_system.repository;

import com.example.pos_system.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findBySale_SaleId(Long saleId); // ✅ correct path
}
