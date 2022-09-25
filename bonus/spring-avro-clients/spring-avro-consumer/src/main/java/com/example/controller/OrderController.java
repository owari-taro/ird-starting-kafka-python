package com.example.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.domain.TicketOrder;

@Component
public class OrderController {
  @KafkaListener(topics = "ticket-order")
  public void consumeOrder(TicketOrder order) {
    String msg = String.format("order_id: %s, user_id: %s, content_id: %s",
        order.getOrderId(), order.getUserId(), order.getContentId());
    System.out.println(msg);
  }
}
