package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topics.order-created}")
    private String orderCreatedTopic;
    
    @Value("${kafka.topics.inventory-reserved}")
    private String inventoryReservedTopic;
    
    @Value("${kafka.topics.billing-processed}")
    private String billingProcessedTopic;

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name(orderCreatedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic inventoryReservedTopic() {
        return TopicBuilder.name(inventoryReservedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic billingProcessedTopic() {
        return TopicBuilder.name(billingProcessedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public String orderCreatedTopic() {
        return orderCreatedTopic;
    }
}