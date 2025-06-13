package com.example.pos_system.service;

import com.example.pos_system.entity.Inventory;
import com.example.pos_system.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            // update fields here, example:
            inventory.setProduct(inventoryDetails.getProduct());
            inventory.setQuantity(inventoryDetails.getQuantity());
            inventory.setLocation(inventoryDetails.getLocation());
            return inventoryRepository.save(inventory);
        }
        return null;
    }

    public boolean deleteInventory(Long id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (inventoryOpt.isPresent()) {
            inventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
