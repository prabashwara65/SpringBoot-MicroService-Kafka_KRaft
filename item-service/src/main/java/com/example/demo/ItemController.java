package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    // Simple in-memory list (no database needed)
    private List<String> items = new ArrayList<>(List.of("Book", "Laptop", "Phone"));

    @GetMapping
    public List<String> getItems() {
        return items;
    }

    @GetMapping("/{id}")
    public String getItemById(@PathVariable int id) {
        if (id >= 0 && id < items.size()) {
            return items.get(id);
        }
        return "Item not found";
    }

    @PostMapping
    public ResponseEntity<String> addItem(@RequestBody String item) {
        items.add(item);
        return ResponseEntity.status(HttpStatus.CREATED).body("Item added: " + item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        if (id >= 0 && id < items.size()) {
            String removedItem = items.remove(id);
            return ResponseEntity.ok("Item removed: " + removedItem);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable int id, @RequestBody String item) {
        if (id >= 0 && id < items.size()) {
            items.set(id, item);
            return ResponseEntity.ok("Item updated: " + item);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }
}