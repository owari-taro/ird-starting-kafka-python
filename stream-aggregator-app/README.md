# イベント集約Streamsアプリケーション

書籍の第7章で説明するStreamsアプリケーションです。
Kafka Topicから受信したイベントを集約して結果を別のTopicに送信します。

## 事前準備

Kafka Topic `category-filtered-ticket-order` と `suspicious-user` を作成してください。

```bash
$ kafka-topics --bootstrap-server broker:9092 \
    --create --topic category-filtered-ticket-order \
    --partitions 3 --replication-factor 1

$ kafka-topics --bootstrap-server broker:9092 \
    --create --topic suspicious-user \
    --partitions 3 --replication-factor 1
```

## Visual Studio Codeからの実行

書籍本文中で説明している手順です。

StreamApp.javaの`main`メソッドのクラスファイルを右クリックして"Run"を選択してください。

## JARファイルからの実行

アプリケーション全体をJARファイルに固めることで、コマンドラインからStreamsアプリケーションを起動可能です。

```bash
$ # JARファイルのビルド
$ mvn clean package -DskipTests

$ # JARファイルからStreamsアプリを起動
$ java -jar target/stream-aggregator-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```
