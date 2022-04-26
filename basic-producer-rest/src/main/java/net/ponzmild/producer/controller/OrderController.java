package net.ponzmild.producer.controller;

import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import net.ponzmild.producer.domain.TicketOrder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static final String TOPIC = "ticket-order";

    private final KafkaProducer<String, String> producer;

    public OrderController(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public Handler createOrder() {
        return ctx -> {
            logger.info("Called create ticket order endpoint.");

            // リクエストからTopicに送信するイベントを組み立て
            CreateOrderRequest request = ctx.bodyAsClass(CreateOrderRequest.class);
            TicketOrder order = new TicketOrder(request.getContentId(), request.getUserId());
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, order.getOrderId(), order.toString());

            // Topicにデータを非同期に送信
            Future<RecordMetadata> sendFuture = producer.send(producerRecord, (metadata, exception) -> {
                if (exception == null) {
                    logger.info("Producer sent record: topic={}, partition={}, offset={}", metadata.topic(), metadata.partition(),
                            metadata.offset());
                } else {
                    exception.printStackTrace();
                    logger.error("Producer got error w/ send()", exception);
                }
            });

            // 結果待ち
            try {
                sendFuture.get(5L, TimeUnit.SECONDS);
                // レスポンスをクライアントに返す
                ctx.status(HttpCode.CREATED).result(order.getOrderId());
            } catch (Exception e) {
                logger.error("Failed to send message!?", e);
                ctx.status(HttpCode.INTERNAL_SERVER_ERROR);
            }
        };
    }
}
