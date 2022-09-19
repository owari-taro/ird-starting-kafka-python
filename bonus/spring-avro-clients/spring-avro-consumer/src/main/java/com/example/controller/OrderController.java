package com.example.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.domain.TicketOrder;

@Component
public class OrderController {
  @KafkaListener(topics = "ticker-order")
  public void consumeOrder(TicketOrder order) {
    String msg = String.format("order_id: {}, user_id: {}, content_id: {}",
        order.getOrderId(), order.getUserId(), order.getContentId());
    System.out.println(msg);
  }
}
