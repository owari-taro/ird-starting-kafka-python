package com.example.service.command;

/**
 * チケット購入ユースケースのコマンドDTO
 */
public class CreateOrderCommand {
  private String userId;
  private String contentId;

  public CreateOrderCommand(String userId, String contentId) {
    this.userId = userId;
    this.contentId = contentId;
  }

  public String getContentId() {
    return contentId;
  }

  public String getUserId() {
    return userId;
  }
}
