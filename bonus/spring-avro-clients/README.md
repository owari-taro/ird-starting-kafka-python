# Kafka Producer with Spring Boot and Avro Schema

このサンプルでは、KafkaクライアントアプリケーションをSpring Bootフレームワークで実装します。
また、KafkaとやりとりするイベントをAvroで定義したスキーマでシリアライズします。スキーマ定義は[Apicurio Registry](https://www.apicur.io/registry/)に格納します。

![概要図](/assets//image/spring-avro-client-overview.png)

## Quick Start

### Kafkaクラスタの構築

Zookeeper、Kafka Broker、Apicurio Registryが含まれる `docker-compose.yaml` を利用して、Kafkaクラスタを構築します。

```bash
$ docker compose up -d
```

クラスタが立ち上がると、以下のコンテナにブラウザからアクセス可能になります。

|コンテナ|URL|
|:--|:--|
|Kafdrop|http://localhost:9000|
|Apicurio Registry|http://localhost:8080|

### アプリケーションのビルド

このサンプルはマルチモジュールのJavaプロジェクトで構成されます。
Producer、Consumer、Domainの3プロジェクトを次のMavenコマンドで一括ビルドします。

```bash
$ mvn clean package -B -ntp

[INFO] Scanning for projects...
...
[INFO] spring-avro-domain 1.0-SNAPSHOT .................... SUCCESS [  3.860 s]
[INFO] spring-avro-producer 1.0.0-SNAPSHOT ................ SUCCESS [  8.094 s]
[INFO] spring-avro-consumer 1.0.0-SNAPSHOT ................ SUCCESS [  7.233 s]
[INFO] spring-avro-clients 1.0-SNAPSHOT ................... SUCCESS [  0.031 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

spring-avro-domainプロジェクトでは、ビルド時に `src/main/avro` ディレクトリに格納したAvroのスキーマ定義をSchema Registryに登録します。
またspring-avro-domainプロジェクトはビルド時にAvroスキーマからイベントのクラス定義を生成し、spring-avro-producerやspring-avro-consumerで使用可能なJARファイルを生成します。

アプリケーションのビルド後にApicurio Registryの画面 http://localhost:8080/ui/artifacts を参照すると、`ticket-order-value` という名前でアーティファクト(スキーマ定義)が登録されています。

![Apicurio Registryの画面](/assets/image/apicurio-registry-artifacts.png)

### アプリケーションの実行

ProducerアプリケーションをJARファイルから起動します。Producerアプリケーションはポート8085番でHTTPリクエストを受け付けます。

```bash
$ java -jar target/spring-avro-producer-1.0.0-SNAPSHOT.jar
```

ConsumerアプリケーションをJARファイルから起動します。ConsumerアプリケーションはKafka Topic `ticket-order` からイベントを読み取ります。

```bash
$ java -jar target/spring-avro-consumer-1.0.0-SNAPSHOT.jar
```

ProducerアプリケーションはREST APIで受け付けたリクエストからイベントを生成します。
`curl` 等のHTTPクライアントアプリケーションでProducerアプリケーションにHTTPリクエストを発行します。
Consumerアプリケーションのログに `order_id` 、 `user_id` 、 `content_id` が出力されます。

```bash
$ curl -X POST -H 'Content-Type: application/json' -d '{"userId": "100", "contentId": "345"}' http://localhost:8085/ticket
```
