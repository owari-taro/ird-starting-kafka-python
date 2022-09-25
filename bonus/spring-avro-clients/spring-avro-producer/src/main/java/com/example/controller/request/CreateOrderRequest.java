package com.example.controller.request;

/**
 * チケット購入APIのリクエストボディ
 */
public class CreateOrderRequest {
  private String contentId;
  private String userId;

  public CreateOrderRequest(String contentId, String userId) {
    this.contentId = contentId;
    this.userId = userId;
  }

  public String getContentId() {
    return contentId;
  }

  public String getUserId() {
    return userId;
  }
}
