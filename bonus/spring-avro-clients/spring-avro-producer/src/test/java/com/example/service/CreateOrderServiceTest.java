package com.example.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.example.service.command.CreateOrderCommand;
import com.example.service.result.CreateOrderResult;

/**
 * チケット購入のユースケースを実現するサービスクラスのテストクラス
 */
@ExtendWith(MockitoExtension.class)
public class CreateOrderServiceTest {
  @InjectMocks
  private CreateOrderService service;

  @Mock
  private KafkaTemplate<String, String> mockedKafkaTemplate;

  @Test
  public void execute_should_send_event_with_command_user_id() {
    // Arrange
    ListenableFuture<SendResult<String, String>> mockFuture = new SettableListenableFuture<>();
    when(mockedKafkaTemplate.send(Mockito.eq("ticket-order"), Mockito.eq("test-user-id-123"), Mockito.anyString())).thenReturn(mockFuture);

    // Act
    CreateOrderCommand command = new CreateOrderCommand("test-user-id-123", "777888999");
    CreateOrderResult result = service.execute(command);

    // Assert
    assertNotNull(result);
    assertNotNull(result.getOrderId());
    verify(mockedKafkaTemplate, times(1)).send(Mockito.eq("ticket-order"), Mockito.eq("test-user-id-123"), Mockito.anyString());
  }
}
