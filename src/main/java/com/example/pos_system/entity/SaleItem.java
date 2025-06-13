package com.example.pos_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sale_item")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @JsonIgnoreProperties({"saleItems", "customer", "user", "hibernateLazyInitializer", "handler"})
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"inventory", "saleItems", "category", "user", "hibernateLazyInitializer", "handler"})
    private Product product;

    private Integer quantity;

    private Double discount; // e.g. 10 for 10%

    private Double total;

    private LocalDateTime saleDate;

    @PrePersist
    public void prePersist() {
        if (saleDate == null) {
            saleDate = LocalDateTime.now();
        }
    }
}
