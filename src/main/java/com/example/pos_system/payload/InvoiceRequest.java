package com.example.pos_system.payload;

import lombok.Data;

@Data
public class InvoiceRequest {
    private Long saleId;
    private double customerCash;
}
