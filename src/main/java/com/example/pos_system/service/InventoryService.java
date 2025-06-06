package com.example.pos_system.service;

import com.example.pos_system.entity.Inventory;
import com.example.pos_system.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Inventory> getAllInventories() {
        return repository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Inventory saveInventory(Inventory inventory) {
        return repository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory updatedInventory) {
        return repository.findById(id)
                .map(inventory -> {
                    inventory.setQuantity(updatedInventory.getQuantity());
                    inventory.setLocation(updatedInventory.getLocation());
                    inventory.setProduct(updatedInventory.getProduct());
                    inventory.setUser(updatedInventory.getUser());
                    return repository.save(inventory);
                })
                .orElse(null);
    }

    public void deleteInventory(Long id) {
        repository.deleteById(id);
    }
}