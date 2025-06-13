package com.example.pos_system.controller;

import com.example.pos_system.entity.Invoice;
import com.example.pos_system.entity.Sale;
import com.example.pos_system.entity.SaleItem;
import com.example.pos_system.payload.InvoiceRequest;
import com.example.pos_system.repository.InvoiceRepository;
import com.example.pos_system.repository.SaleItemRepository;
import com.example.pos_system.repository.SaleRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public void createInvoice(@RequestBody InvoiceRequest request, HttpServletResponse response) throws IOException {
        Long saleId = request.getSaleId();
        double customerCash = request.getCustomerCash();

        // Fetch Sale by ID or throw error
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + saleId));

        // Fetch related SaleItems
        List<SaleItem> items = saleItemRepository.findBySale_SaleId(saleId);

        // Check if any SaleItem has today's date
        boolean hasTodaySale = items.stream().anyMatch(item ->
                item.getSaleDate() != null &&
                        item.getSaleDate().toLocalDate().isEqual(LocalDateTime.now().toLocalDate())
        );

        if (!hasTodaySale) {
            throw new RuntimeException("Invoice can only be created for today's sales.");
        }

        // Calculate totals
        double total = items.stream().mapToDouble(SaleItem::getTotal).sum();
        double returnCash = customerCash - total;

        // Save Invoice data to DB
        Invoice invoice = new Invoice();
        invoice.setSale(sale);
        invoice.setCustomerCash(customerCash);
        invoice.setReturnCash(returnCash);
        invoice.setCreatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        // Prepare directory to save PDFs (relative to app working directory)
        String invoicesDir = "invoices";
        Path invoicesPath = Paths.get(invoicesDir);
        if (!Files.exists(invoicesPath)) {
            Files.createDirectories(invoicesPath);
        }

        String fileName = "invoice_sale_" + saleId + ".pdf";
        Path filePath = invoicesPath.resolve(fileName);

        try {
            // Generate PDF in memory
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Header
            document.add(new Paragraph("INVOICE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            document.add(new Paragraph("Date: " + sale.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            document.add(new Paragraph("Customer: " + sale.getCustomer().getName()));
            document.add(new Paragraph("User: " + sale.getUser().getName()));
            document.add(Chunk.NEWLINE);

            // Sale items table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Product");
            table.addCell("Quantity");
            table.addCell("Discount");
            table.addCell("Total");

            for (SaleItem item : items) {
                table.addCell(item.getProduct().getProductName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(item.getDiscount() + "%");
                table.addCell(String.format("%.2f", item.getTotal()));
            }
            document.add(table);
            document.add(Chunk.NEWLINE);

            // Totals
            document.add(new Paragraph("Total: " + String.format("%.2f", total)));
            document.add(new Paragraph("Customer Cash: " + String.format("%.2f", customerCash)));
            document.add(new Paragraph("Return Cash: " + String.format("%.2f", returnCash)));

            document.close();

            // Save PDF to disk
            Files.write(filePath, baos.toByteArray());

            // Set HTTP response headers & write PDF content
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + ex.getMessage() + "\"}");
    }
}
