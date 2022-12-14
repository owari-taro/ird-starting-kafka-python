# ボーナストラック

このディレクトリは本書では扱えなかったトピックやアプリケーション実装例を格納しています。
本書を読んだ後のボーナストラックとして探索してみましょう。

## コンテンツ

トピックごとにディレクトリ分けされています。

### `rest-producer`

本書で扱ったProducerアプリケーションはターミナル等で操作するコンソールアプリケーションでした。
現実のシステムでも応用可能なREST APIアプリケーションをProducerとするサンプルを提供します。

### `debezium-connect`

第6章ではPostgreSQLのテーブルのレコードをJDBC Connectorでイベントに変換しました。
JDBC Connectorは定期的にSQL文を発行して差分をイベントに変換しているため、イベントの削除を検知することができません。

[Debezium](https://debezium.io/)を使用してデータベースのトランザクションログ（PostgreSQLのWAL）をイベントに変換することで、
レコードの登録、変更、削除を全てイベントに変換するサンプルを提供します。

### `strimzi-kubernetes`

KubernetesにKafkaをデプロイするサンプルを提供します。
Kafkaのコンポーネントは[Strimzi](https://strimzi.io/)というKubernetes Operatorで作成します。

Kubernetesのマニフェストで宣言的にKafkaクラスタ、Topic、Kafka Connectクラスタ、Connectorを作成します。
