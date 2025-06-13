
package com.example.pos_system.repository;

import com.example.pos_system.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findFirstByProduct_Id(Long productId); // Use this to avoid "multiple results" error
}
