# Kafka Consumerアプリケーション

書籍の第4章で説明するConsumerアプリケーションです。
Kafkaクラスタからイベントを受信します。

## 事前準備

Kafka Topic `ticket-order` を作成してください。

```bash
$ kafka-topics --bootstrap-server broker:9092 \
    --create --topic ticket-order \
    --partitions 3 --replication-factor 1
```

## Visual Studio Codeからの実行

書籍本文中で説明している手順です。

ConsumerApp.javaの`main`メソッドのクラスファイルを右クリックして"Run"を選択してください。

## JARファイルからの実行

アプリケーション全体をJARファイルに固めることで、コマンドラインからConsumerを起動可能です。

```bash
$ # JARファイルのビルド
$ mvn clean package -DskipTests

$ # JARファイルからConsumerを起動
$ java -jar target/basic-consumer-1.0-SNAPSHOT-jar-with-dependencies.jar
```
