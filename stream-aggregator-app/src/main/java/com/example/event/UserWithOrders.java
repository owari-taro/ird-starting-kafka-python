package com.example.event;

public class UserWithOrders {
  private String userId;
  private Integer orderCount;

  public UserWithOrders() {
    this.orderCount = 0;
  }

  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public Integer getOrderCount() {
    return orderCount;
  }
  public void setOrderCount(Integer orderCount) {
    this.orderCount = orderCount;
  }  
}
