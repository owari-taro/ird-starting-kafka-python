package net.ponzmild.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import net.ponzmild.producer.domain.TicketOrder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OrderController {
    private static final String TOPIC = "ticket-order";

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderController(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public void createOrder(Context ctx) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        // リクエストからTopicに送信するイベントを組み立て
        CreateOrderRequest request = ctx.bodyAsClass(CreateOrderRequest.class);
        TicketOrder order = new TicketOrder(request.getContentId(), request.getUserId());
        String recordValueString = mapper.writeValueAsString(order);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, order.getUserId(), recordValueString);

        // Topicにデータを非同期に送信
        Future<RecordMetadata> sendFuture = producer.send(producerRecord);
        // 結果待ち
        sendFuture.get(5L, TimeUnit.SECONDS);

        // レスポンスをクライアントに返す
        ctx.status(HttpCode.CREATED).result(order.getOrderId());
    }
}
