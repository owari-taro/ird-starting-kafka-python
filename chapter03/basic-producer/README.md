# Kafka Producerアプリケーション

書籍の第3章で説明するProducerアプリケーションです。
Kafkaクラスタにイベントを送信します。

## Visual Studio Codeからの実行

書籍本文中で説明している手順です。

ProducerApp.javaの`main`メソッドのクラスファイルを右クリックして"Run"を選択してください。

## JARファイルからの実行

アプリケーション全体をJARファイルに固めることで、コマンドラインからProducerを起動可能です。

```bash
$ # JARファイルのビルド
$ mvn clean package -DskipTests

$ # JARファイルからProducerを起動
$ java -jar target/basic-producer-1.0-SNAPSHOT-jar-with-dependencies.jar
```
