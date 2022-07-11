package com.example;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.event.TicketOrder;
import com.example.event.User;
import com.example.event.UserWithOrders;
import com.example.event.UserWithOrdersItem;
import com.example.serdes.TicketOrderSerdes;
import com.example.serdes.UserSerdes;

/**
 * ストリーム処理のエントリーポイント
 */
public class StreamApp {
    private static final Logger logger = LoggerFactory.getLogger(StreamApp.class.getName());
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:29092";
    private static final String SOURCE_ORDER_TOPIC = "category-filtered-ticket-order";
    private static final String SOURCE_USER_TOPIC = "category-filtered-ticket-order";
    private static final String SINK_TOPIC = "suspicious-ticket-order";

    public static void main(String[] args) {
        // プロセッサー・トポロジーをビルド
        Topology topology = buildTopology();
        logger.info(topology.describe().toString());

        // 設定値を取得
        Properties streamConfig = getStreamsConfig();

        // 設定値とトポロジーを組み合わせてKafka Streamsアプリケーションを定義
        KafkaStreams streams = new KafkaStreams(topology, streamConfig);
        streams.start();

        // グレースフル・シャットダウン
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static Properties getStreamsConfig() {
        Properties streamConfig = new Properties();

        // (1) 必須の設定値
        streamConfig.setProperty("application.id", "filter-streams-app");
        streamConfig.setProperty("bootstrap.server", BOOTSTRAP_SERVERS);

        // (2) デフォルトで使用するイベントのキーと値のシリアライズ/デシリアライズ方法を定義
        streamConfig.setProperty("default.key.serde", Serdes.String().getClass().getName());
        streamConfig.setProperty("default.value.serde", Serdes.String().getClass().getName());
        return streamConfig;
    }

    private static Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // ① ソースプロセッサー (Ticket Order)
        KStream<String, TicketOrder> ticketOrderStream = streamsBuilder.stream(SOURCE_ORDER_TOPIC, Consumed.with(Serdes.String(), new TicketOrderSerdes()));

        // ② ストリームプロセッサー
        KStream<String, TicketOrder> aggregatedStream = ticketOrderStream.peek((key, ticketOrder) -> logger.info("Read event w/ key={}", key));

        // ① ソースプロセッサー (User)
        KTable<String, User> userTable = streamsBuilder.table(SOURCE_USER_TOPIC, Consumed.with(Serdes.String(), new UserSerdes()));

        // ② ストリームプロセッサー
        ticketOrderStream
            .groupByKey()
            .windowedBy(SlidingWindows.ofTimeDifferenceAndGrace(Duration.ofMinutes(30L), Duration.ofMinutes(5L)))
            .aggregate(UserWithOrders::new, (key, ticketOrder, userWithOrders) -> {
                List<UserWithOrdersItem> orders = userWithOrders.getOrders();
                UserWithOrdersItem order = new UserWithOrdersItem();
                order.setOrderId(ticketOrder.getOrderId());
                order.setContentId(ticketOrder.getContentId());

                userWithOrders.setUserId(key);
                userWithOrders.setOrders(orders);
                return userWithOrders;
            });

        // ③ シンクプロセッサー
        aggregatedStream.to(SINK_TOPIC, Produced.with(Serdes.String(), new TicketOrderSerdes()));

        // プロセッサー・トポロジーをビルド
        return streamsBuilder.build();
    }
}
