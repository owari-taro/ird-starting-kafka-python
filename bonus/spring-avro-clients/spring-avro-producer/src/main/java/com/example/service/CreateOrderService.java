package com.example.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.example.service.command.CreateOrderCommand;
import com.example.service.result.CreateOrderResult;

/**
 * チケット購入のユースケースを実現するサービスクラス
 */
@Service
public class CreateOrderService {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public CreateOrderService(KafkaTemplate<String, String> kafkaTemplate) {
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
    String eventValue = String.format("order_id = %s, user_id = %s, content_id = %s", orderId, userId, contentId);

    kafkaTemplate.send("ticket-order", userId, eventValue)
      .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
        @Override
        public void onSuccess(SendResult<String, String> result) {
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
