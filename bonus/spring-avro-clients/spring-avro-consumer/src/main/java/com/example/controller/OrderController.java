package com.example.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderController {
  @KafkaListener(topics = "ticker-order")
  public void consumeOrder(String order) {
    System.out.println(order);
  }
}
