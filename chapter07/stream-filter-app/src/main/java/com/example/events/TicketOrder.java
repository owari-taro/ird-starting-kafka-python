package com.example.events;

/**
 * チケット取引イベント
 */
public class TicketOrder {
  private String orderId;
  private String userId;
  private String contentId;
  private String category;

  public String getOrderId() {
    return orderId;
  }
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getContentId() {
    return contentId;
  }
  public void setContentId(String contentId) {
    this.contentId = contentId;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }

}
