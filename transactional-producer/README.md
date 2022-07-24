# イベント集約Streamsアプリケーション

書籍の第5章で説明するProducerアプリケーションです。
トランザクション機能を有効化したProducerであり、Topicにイベントを送信します。

## 事前準備

Kafkaでトランザクションを有効化するには、Topicのレプリカ数（replication factor）が3以上である必要があります。1台構成のKafkaクラスタを使用している場合は、複数台構成のKafkaクラスタを立ててください。

複数台構成のKafkaクラスタの構築方法は、
当リポジトリの `setup/docker/multi-broker-cluster` に記載しています。

また、Kafka Topic `ticket-order` を作成してください。

```bash
$ kafka-topics --bootstrap-server broker-1:9092 \
    --create --topic ticket-order \
    --partitions 6 --replication-factor 3
```

## Visual Studio Codeからの実行

書籍本文中で説明している手順です。

StreamApp.javaの`main`メソッドのクラスファイルを右クリックして"Run"を選択してください。

## JARファイルからの実行

アプリケーション全体をJARファイルに固めることで、コマンドラインからStreamsアプリケーションを起動可能です。

```bash
$ # JARファイルのビルド
$ mvn clean package -DskipTests

$ # JARファイルからProducerアプリを起動
$ java -jar target/transactional-producer-1.0-SNAPSHOT-jar-with-dependencies.jar
```
