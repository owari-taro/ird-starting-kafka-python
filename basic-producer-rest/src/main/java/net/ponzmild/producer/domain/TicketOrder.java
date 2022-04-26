package net.ponzmild.producer.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class TicketOrder {
    private final String orderId;
    private final String contentId;
    private final String userId;

    public TicketOrder(String contentId, String userId) {
        this.orderId = UUID.randomUUID().toString();
        this.contentId = contentId;
        this.userId = userId;
    }
}
