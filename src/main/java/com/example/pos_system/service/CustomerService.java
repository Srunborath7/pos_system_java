package com.example.pos_system.service;

import com.example.pos_system.entity.Customer;
import com.example.pos_system.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return repository.findById(id)
                .map(customer -> {
                    customer.setName(updatedCustomer.getName());
                    customer.setPhone(updatedCustomer.getPhone());
                    customer.setAddress(updatedCustomer.getAddress());
                    customer.setUser(updatedCustomer.getUser());
                    return repository.save(customer);
                })
                .orElse(null);
    }

    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }
}
