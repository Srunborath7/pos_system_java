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
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public SaleItemService(SaleItemRepository saleItemRepository,
                           ProductRepository productRepository,
                           InventoryRepository inventoryRepository) {
        this.saleItemRepository = saleItemRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<SaleItem> getAllSaleItems() {
        return saleItemRepository.findAll();
    }

    public Optional<SaleItem> getSaleItemById(Long id) {
        return saleItemRepository.findById(id);
    }

    public SaleItem saveSaleItem(SaleItem saleItem) throws Exception {
        Product product = productRepository.findById(saleItem.getProduct().getId())
                .orElseThrow(() -> new Exception("Product not found"));

        if (product.getPrice() == null) {
            throw new Exception("Product price must not be null");
        }

        Integer quantity = saleItem.getQuantity();
        if (quantity == null || quantity <= 0) {
            throw new Exception("Quantity must be greater than zero");
        }

        Double discount = saleItem.getDiscount() != null ? saleItem.getDiscount() : 0.0;
        if (discount < 0 || discount > 100) {
            throw new Exception("Discount must be between 0 and 100");
        }

        // Use new method to avoid multiple results error
        Inventory inventory = inventoryRepository.findFirstByProduct_Id(product.getId())
                .orElseThrow(() -> new Exception("Inventory not found for product"));

        if (inventory.getQuantity() < quantity) {
            throw new Exception("Insufficient inventory quantity");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        double total = quantity * product.getPrice() * (1 - discount / 100.0);
        saleItem.setTotal(total);
        saleItem.setSaleDate(LocalDateTime.now());
        saleItem.setProduct(product);

        return saleItemRepository.save(saleItem);
    }

    public SaleItem updateSaleItem(Long id, SaleItem saleItem) throws Exception {
        SaleItem existing = saleItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Sale item not found"));

        Inventory inventory = inventoryRepository.findFirstByProduct_Id(existing.getProduct().getId())
                .orElseThrow(() -> new Exception("Inventory not found for product"));

        // Restore old quantity
        inventory.setQuantity(inventory.getQuantity() + existing.getQuantity());

        Integer newQuantity = saleItem.getQuantity();
        if (newQuantity == null || newQuantity <= 0) {
            throw new Exception("Quantity must be greater than zero");
        }

        Double discount = saleItem.getDiscount() != null ? saleItem.getDiscount() : 0.0;
        if (discount < 0 || discount > 100) {
            throw new Exception("Discount must be between 0 and 100");
        }

        Product product = productRepository.findById(saleItem.getProduct().getId())
                .orElseThrow(() -> new Exception("Product not found"));

        if (inventory.getQuantity() < newQuantity) {
            throw new Exception("Insufficient inventory quantity for update");
        }

        inventory.setQuantity(inventory.getQuantity() - newQuantity);
        inventoryRepository.save(inventory);

        double total = newQuantity * product.getPrice() * (1 - discount / 100.0);

        existing.setQuantity(newQuantity);
        existing.setDiscount(discount);
        existing.setTotal(total);
        existing.setSaleDate(LocalDateTime.now());
        existing.setProduct(product);

        return saleItemRepository.save(existing);
    }

    public void deleteSaleItem(Long id) throws Exception {
        SaleItem existing = saleItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Sale item not found"));

        Inventory inventory = inventoryRepository.findFirstByProduct_Id(existing.getProduct().getId())
                .orElseThrow(() -> new Exception("Inventory not found for product"));

        inventory.setQuantity(inventory.getQuantity() + existing.getQuantity());
        inventoryRepository.save(inventory);

        saleItemRepository.deleteById(id);
    }
}
