package com.example.pos_system.service;

import com.example.pos_system.entity.Inventory;
import com.example.pos_system.entity.Product;
import com.example.pos_system.entity.SaleItem;
import com.example.pos_system.repository.InventoryRepository;
import com.example.pos_system.repository.ProductRepository;
import com.example.pos_system.repository.SaleItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleItemService {

    private final SaleItemRepository saleItemRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public SaleItemService(SaleItemRepository saleItemRepository,
                           InventoryRepository inventoryRepository,
                           ProductRepository productRepository) {
        this.saleItemRepository = saleItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public List<SaleItem> getAllSaleItems() {
        return saleItemRepository.findAll();
    }

    public Optional<SaleItem> getSaleItemById(Long id) {
        return saleItemRepository.findById(id);
    }

    public SaleItem saveSaleItem(SaleItem saleItem) {
        Product product = productRepository.findById(saleItem.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Double priceObj = product.getPrice();
        if (priceObj == null) {
            throw new RuntimeException("Product price is not set for product id " + product.getId());
        }
        double price = priceObj;

        Inventory inventory = inventoryRepository.findByProductId(saleItem.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id " + saleItem.getProduct().getId()));
        
        int newQty = inventory.getQuantity() - saleItem.getQuantity();
        if (newQty < 0) {
            throw new RuntimeException("Not enough inventory for product id " + product.getId());
        }

        inventory.setQuantity(newQty);
        inventoryRepository.save(inventory);

        // Calculate total using percentage discount
        double discountPercent = saleItem.getDiscount() != null ? saleItem.getDiscount() : 0.0;
        double totalBeforeDiscount = price * saleItem.getQuantity();
        double discountAmount = totalBeforeDiscount * (discountPercent / 100.0);
        double total = totalBeforeDiscount - discountAmount;
        saleItem.setTotal(total);

        if (saleItem.getSaleDate() == null) {
            saleItem.setSaleDate(LocalDateTime.now());
        }

        return saleItemRepository.save(saleItem);
    }

    public SaleItem updateSaleItem(Long id, SaleItem updatedSaleItem) {
        return saleItemRepository.findById(id).map(existing -> {
            Inventory inventory = inventoryRepository.findByProductId(existing.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product id " + existing.getProduct().getId()));

            inventory.setQuantity(inventory.getQuantity() + existing.getQuantity());
            inventoryRepository.save(inventory);

            Product product = productRepository.findById(updatedSaleItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Inventory newInventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product id " + product.getId()));

            int availableQty = newInventory.getQuantity();
            int newQty = updatedSaleItem.getQuantity();

            if (newQty > availableQty) {
                throw new RuntimeException("Insufficient quantity in inventory. Available: " + availableQty);
            }

            double price = product.getPrice();
            double discountPercent = updatedSaleItem.getDiscount() != null ? updatedSaleItem.getDiscount() : 0.0;
            double totalBeforeDiscount = price * newQty;
            double discountAmount = totalBeforeDiscount * (discountPercent / 100.0);
            double total = totalBeforeDiscount - discountAmount;

            newInventory.setQuantity(availableQty - newQty);
            inventoryRepository.save(newInventory);

            existing.setSale(updatedSaleItem.getSale());
            existing.setProduct(product);
            existing.setQuantity(newQty);
            existing.setDiscount(discountPercent);
            existing.setTotal(total);

            return saleItemRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("SaleItem not found with id " + id));
    }

    public void deleteSaleItem(Long id) {
        saleItemRepository.findById(id).ifPresent(saleItem -> {
            Inventory inventory = inventoryRepository.findByProductId(saleItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product id " + saleItem.getProduct().getId()));

            inventory.setQuantity(inventory.getQuantity() + saleItem.getQuantity());
            inventoryRepository.save(inventory);

            saleItemRepository.delete(saleItem);
        });
    }
}
