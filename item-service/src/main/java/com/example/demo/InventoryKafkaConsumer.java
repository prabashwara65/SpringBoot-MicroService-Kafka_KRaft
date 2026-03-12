package com.example.demo;

import com.example.events.OrderEvent;
import com.example.events.InventoryEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryKafkaConsumer {

    @Autowired
    private ItemController itemController;
    
    @Autowired
    private KafkaTemplate<String, InventoryEvent> kafkaTemplate;
    
    @Value("${kafka.topics.inventory-reserved}")
    private String inventoryReservedTopic;

    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "inventory-service-group")
    public void consumeOrderCreated(OrderEvent event) {
        System.out.println("========== INVENTORY SERVICE RECEIVED ORDER ==========");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Product ID: " + event.getProductId());
        System.out.println("Quantity: " + event.getQuantity());
        
        // Check if item exists in our list
        boolean itemExists = false;
        try {
            int index = Integer.parseInt(event.getProductId().replaceAll("[^0-9]", ""));
            String item = itemController.getItemById(index);
            if (!item.contains("not found")) {
                itemExists = true;
            }
        } catch (Exception e) {
            System.out.println("Item check failed: " + e.getMessage());
        }
        
        InventoryEvent inventoryEvent;
        if (itemExists) {
            inventoryEvent = new InventoryEvent(
                event.getProductId(),
                event.getQuantity(),
                "RESERVED"
            );
            System.out.println("✅ Inventory reserved for product: " + event.getProductId());
        } else {
            inventoryEvent = new InventoryEvent(
                event.getProductId(),
                event.getQuantity(),
                "FAILED"
            );
            System.out.println("❌ Inventory reservation failed - product not found: " + event.getProductId());
        }
        
        // Send inventory event
        kafkaTemplate.send(inventoryReservedTopic, event.getOrderId(), inventoryEvent);
        System.out.println("📤 Inventory event sent to Kafka");
    }
}