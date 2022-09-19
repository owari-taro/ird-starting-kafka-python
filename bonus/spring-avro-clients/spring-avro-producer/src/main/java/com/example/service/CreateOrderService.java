package com.example.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.example.domain.TicketOrder;
import com.example.service.command.CreateOrderCommand;
import com.example.service.result.CreateOrderResult;

/**
 * チケット購入のユースケースを実現するサービスクラス
 */
@Service
public class CreateOrderService {
  private final KafkaTemplate<String, TicketOrder> kafkaTemplate;

  public CreateOrderService(KafkaTemplate<String, TicketOrder> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * チケット購入処理を実行する。
   * @param command チケット購入DTO
   * @return チケット購入処理結果DTO
   */
  public CreateOrderResult execute(CreateOrderCommand command) {
    final String orderId = UUID.randomUUID().toString();
    String userId = command.getUserId();
    String contentId = command.getContentId();

    TicketOrder order = TicketOrder.newBuilder()
      .setOrderId(orderId)
      .setUserId(userId)
      .setContentId(contentId)
      .build();

    kafkaTemplate.send("ticket-order", userId, order)
      .addCallback(new ListenableFutureCallback<SendResult<String, TicketOrder>>() {
        @Override
        public void onSuccess(SendResult<String, TicketOrder> result) {
          System.out.println("Sent event successfully!!");
        }

        @Override
        public void onFailure(Throwable ex) {
          System.out.println("Sending event failed...");
        }
      });

    return new CreateOrderResult(orderId);
  }
}
