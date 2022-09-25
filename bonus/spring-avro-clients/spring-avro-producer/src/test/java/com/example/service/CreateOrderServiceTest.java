package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.example.domain.TicketOrder;
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
  private KafkaTemplate<String, TicketOrder> mockedKafkaTemplate;

  @Test
  public void execute_should_send_event_with_command_user_id() {
    // Arrange
    ArgumentCaptor<TicketOrder> eventValueCaptor = ArgumentCaptor.forClass(TicketOrder.class);
    ListenableFuture<SendResult<String, TicketOrder>> mockFuture = new SettableListenableFuture<>();
    when(mockedKafkaTemplate.send(Mockito.eq("ticket-order"), Mockito.eq("test-user-id-123"), eventValueCaptor.capture())).thenReturn(mockFuture);

    // Act
    CreateOrderCommand command = new CreateOrderCommand("test-user-id-123", "777888999");
    CreateOrderResult result = service.execute(command);

    // Assert response
    assertNotNull(result);
    assertNotNull(result.getOrderId());

    // Assert mock
    verify(mockedKafkaTemplate, times(1)).send(Mockito.eq("ticket-order"), Mockito.eq("test-user-id-123"), Mockito.any(TicketOrder.class));
    TicketOrder actualOrder = eventValueCaptor.getValue();
    assertEquals("test-user-id-123", actualOrder.getUserId());
    assertEquals("777888999", actualOrder.getContentId());
  }
}
