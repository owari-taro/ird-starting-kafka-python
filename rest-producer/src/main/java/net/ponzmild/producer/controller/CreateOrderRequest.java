package net.ponzmild.producer.controller;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CreateOrderRequest {
    private String contentId;
    private String userId;
}
