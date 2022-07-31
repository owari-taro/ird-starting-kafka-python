package net.ponzmild.producer;

import io.javalin.Javalin;
import io.javalin.http.HttpCode;
import io.javalin.plugin.metrics.MicrometerPlugin;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import net.ponzmild.producer.client.KafkaProducerFactory;
import net.ponzmild.producer.controller.OrderController;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerWebApp {
    private static final Logger logger = LoggerFactory.getLogger(ProducerWebApp.class);
    // KafkaProducerはスレッドセーフなのでシングルトンで生成して使い回す
    private static final KafkaProducer<String, String> producer = KafkaProducerFactory.newInstance();
    private static final OrderController resource = new OrderController(producer);

    public static void main(String[] args) {
        // Plugin設定
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new MicrometerPlugin(registry));
        });

        // メトリクス取得
        app.get("/metrics", ctx -> {
            ctx.contentType(TextFormat.CONTENT_TYPE_004).result(registry.scrape());
        });

        // ルーティング事前処理
        app.before(ctx -> {
            logger.info("Called endpoint: {}", ctx.path());
        });

        // イベント処理 ルーティング
        app.post("/ticket", resource::createOrder);

        // エラー発生時のデフォルトルーティング
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Got error.", e);
            ctx.status(HttpCode.INTERNAL_SERVER_ERROR);
        });

        // シャットダウン処理を登録
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
        app.events(event -> {
            event.serverStopping(() -> {
                producer.flush();
                producer.close();
            });
        });

        // サーバ起動
        app.start(7070);
    }
}
