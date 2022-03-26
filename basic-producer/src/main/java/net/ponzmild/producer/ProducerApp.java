package net.ponzmild.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerApp {
    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class);
    private static final String TOPIC = "sample-topic";

    public static void main(String[] args) {
        logger.info("Producer started.");

        // 送信するデータを組み立てる
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, "key1", "value");

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
