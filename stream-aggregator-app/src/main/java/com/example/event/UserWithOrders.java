package com.example.event;

import java.util.List;

public class UserWithOrders {
  private String userId;
  private List<UserWithOrdersItem> orders;

  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public List<UserWithOrdersItem> getOrders() {
    return orders;
  }
  public void setOrders(List<UserWithOrdersItem> orders) {
    this.orders = orders;
  }
}
