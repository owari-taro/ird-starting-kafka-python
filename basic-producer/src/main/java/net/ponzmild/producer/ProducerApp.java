package net.ponzmild.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ProducerApp {
    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class);
    private static final String TOPIC = "ticket-order";

    public static void main(String[] args) {
        logger.info("Producer started.");

        // 送信するデータを組み立てる
        final String orderId = UUID.randomUUID().toString();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, orderId, "value");

        // Topicにデータを送信
        KafkaProducer<String, String> producer = KafkaProducerFactory.newInstance();
        producer.send(producerRecord, (metadata, exception) -> {
            if (exception == null) {
                logger.info("Producer sent record: topic={}, partition={}, offset={}", metadata.topic(), metadata.partition(),
                        metadata.offset());
            } else {
                exception.printStackTrace();
                logger.error("Producer got error w/ send()", exception);
            }
        });

        // Producerのバッファに溜まっているデータをTopicに全て送信しきる
        producer.flush();

        // Kafkaクラスタとの接続を切る
        producer.close();
        logger.info("Producer shutdown...");
    }
}
