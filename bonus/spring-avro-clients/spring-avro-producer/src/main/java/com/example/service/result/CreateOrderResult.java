package com.example.service.result;

/**
 * チケット購入ユースケースの処理結果DTO
 */
public class CreateOrderResult {
  private String orderId;

  public CreateOrderResult(String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }
}
