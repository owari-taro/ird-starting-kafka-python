package net.ponzmild.producer.client;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * KafkaProducerのインスタンスを作成するファクトリクラス.
 */
public class KafkaProducerFactory {
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:29092";
    private static final String SERIALIZER_NAME = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String CLIENT_ID = "basic-producer-rest-1";
    private static final KafkaProducer<String, String> INSTANCE = new KafkaProducer<>(getProducerConfig());

    private KafkaProducerFactory() {
    }

    /**
     * KafkaProducerのインスタンスを返すファクトリメソッド.
     * インスタンスはシングルトンである.
     * @return KafkaProducerインスタンス
     */
    public static KafkaProducer<String, String> newInstance() {
        return INSTANCE;
    }

    private static Properties getProducerConfig() {
        Properties producerConfig = new Properties();
        producerConfig.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerConfig.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER_NAME);
        producerConfig.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER_NAME);
        producerConfig.setProperty(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        producerConfig.setProperty(ProducerConfig.RETRIES_CONFIG, "5");

        return producerConfig;
    }
}
