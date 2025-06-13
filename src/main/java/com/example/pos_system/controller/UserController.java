package com.example.pos_system.controller;

import com.example.pos_system.entity.Category;
import com.example.pos_system.entity.User;
import com.example.pos_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.status(201).body(Map.of(
                "message", "User inserted successfully",
                "data", savedUser
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            User updatedUser = userService.save(user);
            return ResponseEntity.ok(Map.of(
                    "message", "User updated successfully",
                    "data", updatedUser
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return userService.findById(id).map(user -> {
            userService.deleteById(id);
            return ResponseEntity.ok(Map.of(
                    "message", "User deleted successfully"
            ));
        }).orElse(ResponseEntity.notFound().build());
    }


}
