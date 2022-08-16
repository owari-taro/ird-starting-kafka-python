# Debeziumによるデータベースレコードのイベント変換

## 概要

[Debezium](https://debezium.io/)はデータベースをイベントに変換するChange Data Capture (CDC) を実現するオープンソースのソリューションです。
DebeziumはKafka ConnectのConnector Pluginとして実装されています。
DebeziumはPostgreSQL、MySQL、Oracle、Db2といったリレーショナルデータベースだけでなく、
MongoDBやCassandraといったNoSQLデータベースもサポートしています。

このディレクトリではDebeziumを使用してデータベースのトランザクションログ（PostgreSQLのWAL）をイベントに変換することで、
レコードの登録、変更、削除をイベントに変換するサンプルを提供します。

Debeziumのアーキテクチャ、およびPostgreSQL向けの詳細な設定は以下のリンクを参照してください。

* [Debeziumのアーキテクチャ](https://debezium.io/documentation/reference/stable/architecture.html)
* [Debezium connector for PostgreSQL](https://debezium.io/documentation/reference/stable/connectors/postgresql.html)
* [提供するConnectorとデータベースの対応](https://debezium.io/documentation/reference/stable/connectors/index.html)

## Getting Started

Docker ComposeでKafkaクラスタ、Kafka ConnectクラスタおよびPostgreSQLを起動します。

```shell
$ docker compose up -d
```

Connector Plugin一覧にDebezium connector for PostgreSQLが含まれています。

```shell
$ curl -s http://localhost:8083/connector-plugins/ | jq .
[
  {
    "class": "io.debezium.connector.postgresql.PostgresConnector",
    "type": "source",
    "version": "1.9.5.Final"
  },
  ...
]
```

Connectorインスタンスを起動します。

```shell
$ curl -X POST \                                         
    --url http://localhost:8083/connectors \
    -H 'content-type: application/json' \
    -d @create_source_connector.json
{"name":"postgresql-irdappdb-source-connector",...,"type":"source"}

$ curl -s http://localhost:8083/connectors/postgresql-irdappdb-source-connector/status | jq .
{
  "name": "postgresql-irdappdb-source-connector",
  "connector": {
    "state": "RUNNING",
    "worker_id": "172.18.0.8:8083"
  },
  "tasks": [
    {
      "id": 0,
      "state": "RUNNING",
      "worker_id": "172.18.0.8:8083"
    }
  ],
  "type": "source"
}
```

テーブル一覧を確認して、任意のレコードを投入します。
このサンプルでは、`users`、`contents`、`ticket_orders`の3テーブルが作成されています。

```shell
$ docker exec postgresql psql -U appuser -d irdappdb -c '\d'
                    List of relations
 Schema |            Name            |   Type   |  Owner  
--------+----------------------------+----------+---------
 public | contents                   | table    | appuser
 public | contents_content_id_seq    | sequence | appuser
 public | ticket_orders              | table    | appuser
 public | ticket_orders_order_id_seq | sequence | appuser
 public | users                      | table    | appuser
 public | users_user_id_seq          | sequence | appuser
(6 rows)

$ docker exec postgresql psql -U appuser -d irdappdb -c 'INSERT INTO users VALUES (...);'
$ docker exec postgresql psql -U appuser -d irdappdb -c 'INSERT INTO contents VALUES (...);'
$ docker exec postgresql psql -U appuser -d irdappdb -c 'INSERT INTO ticket_orders VALUES (...);'
```

テーブルにレコードをINSERTすると、`<サーバー名>.<スキーマ名>.<テーブル名>` というKafka Topicが作成されます。
これらのKafka TopicにPostgreSQLのテーブルのレコードがニアリアルタイムに連携されます。

```shell
$ docker exec cli kafka-topics --bootstrap-server broker-1:9092 --list                                
__consumer_offsets
connect-config
connect-offsets
connect-status
irdappdb.public.contents
irdappdb.public.ticket_orders
irdappdb.public.users
```
