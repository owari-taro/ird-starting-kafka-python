package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.controller.request.CreateOrderRequest;
import com.example.service.CreateOrderService;
import com.example.service.command.CreateOrderCommand;
import com.example.service.result.CreateOrderResult;

/**
 * チケット購入に関する操作をまとめたAPIコントローラクラスのテストクラス
 */
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
  @InjectMocks
  private OrderController controller;

  @Mock
  private CreateOrderService mockedService;

  @Test
  public void createOrder_should_call_service_with_request_parameters() {
    // Arrange
    ArgumentCaptor<CreateOrderCommand> commandCaptor = ArgumentCaptor.forClass(CreateOrderCommand.class);
    when(mockedService.execute(commandCaptor.capture()))
        .thenReturn(new CreateOrderResult("7b2d84a6-3740-4fb2-96a7-5949d775eb0c"));

    // Act
    CreateOrderRequest request = new CreateOrderRequest("testuserid001", "testcontentid222");
    String response = controller.createOrder(request);

    // Assert response
    assertEquals("7b2d84a6-3740-4fb2-96a7-5949d775eb0c", response);

    // Assert mock
    CreateOrderCommand acutalCommand = commandCaptor.getValue();
    assertEquals("testuserid001", acutalCommand.getUserId());
    assertEquals("testcontentid222", acutalCommand.getContentId());
  }
}
