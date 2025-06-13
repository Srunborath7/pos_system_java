package com.example.pos_system.controller;

import com.example.pos_system.entity.Customer;
import com.example.pos_system.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.status(404).body("Customer with ID " + id + " not found");
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.saveCustomer(customer);
            return ResponseEntity.status(201).body(
                    Map.of("message", "Customer created successfully", "data", savedCustomer)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Failed to create customer", "error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer == null) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "Customer with ID " + id + " not found")
            );
        }
        return ResponseEntity.ok(
                Map.of("message", "Customer updated successfully", "data", updatedCustomer)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.ok(
                    Map.of("message", "Customer with ID " + id + " deleted successfully")
            );
        } else {
            return ResponseEntity.status(404).body(
                    Map.of("message", "Customer with ID " + id + " not found")
            );
        }
    }

}
