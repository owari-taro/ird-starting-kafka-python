package com.example.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.controller.request.CreateOrderRequest;
import com.example.service.CreateOrderService;
import com.example.service.command.CreateOrderCommand;
import com.example.service.result.CreateOrderResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * チケット購入に関する操作をまとめたAPIコントローラクラス
 */
@RestController
public class OrderController {
  private final CreateOrderService createOrderService;

  public OrderController(CreateOrderService createOrderService) {
    this.createOrderService = createOrderService;
  }

  /**
   * チケット購入API
   * @param request チケット購入リクエストのボディ
   * @return 購入ID
   */
  @PostMapping(value="/ticket")
  public String createOrder(@RequestBody CreateOrderRequest request) {
    final String userId = request.getUserId();
    final String contentId = request.getContentId();
    CreateOrderCommand command = new CreateOrderCommand(contentId, userId);

    CreateOrderResult result = createOrderService.execute(command);

    return result.getOrderId();
  }
}
