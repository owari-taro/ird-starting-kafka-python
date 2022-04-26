package net.ponzmild.producer;

import io.javalin.Javalin;
import net.ponzmild.producer.client.KafkaProducerFactory;
import net.ponzmild.producer.controller.OrderController;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerWebApp {
    private static final Logger logger = LoggerFactory.getLogger(ProducerWebApp.class);
    // KafkaProducerはスレッドセーフなのでシングルとんで生成して使い回す
    private static final KafkaProducer<String, String> producer = KafkaProducerFactory.newInstance();
    private static final OrderController resource = new OrderController(producer);

    public static void main(String[] args) {
        Javalin app = Javalin.create().events(event -> {
            event.serverStopping(() -> {
                logger.info("Kafka producer stopping...");
                producer.flush();
                producer.close();
            });
        });

        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
        app.start(7070);

        app.post("/ticket", resource.createOrder());
    }
}
