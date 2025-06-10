package com.example.pos_system.controller;

import com.example.pos_system.entity.Customer;
import com.example.pos_system.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customer> getAll() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity <String>create(@RequestBody Customer customer) {
        service.saveCustomer(customer);
        return ResponseEntity.status(201).body("User inserted successfully");
    }

    @PutMapping("/{id}")
    public  ResponseEntity <String> update(@PathVariable Long id, @RequestBody Customer customer) {
        service.updateCustomer(id, customer);
        return ResponseEntity.status(201).body("User Updated successfully");
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity <String> delete(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.status(204).body("User Deleted successfully");
    }


}
