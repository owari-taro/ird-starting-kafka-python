package net.ponzmild.producer.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
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
