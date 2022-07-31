# Kafka Producer Webアプリケーション

## 概要

REST APIでイベントを送信するProducerアプリケーションです。
書籍の第3章で説明したProducerアプリケーションを拡張し、
イベントの値の一部をHTTPリクエストから渡します。

Webアプリケーションフレームワークとして、[Javalin](https://javalin.io/)を採用しています。


## 起動方法

### Visual Studio Codeからの実行

ProducerWebApp.javaの`main`メソッドのクラスファイルを右クリックして"Run"を選択してください。

### JARファイルからの実行

アプリケーション全体をJARファイルに固めることで、コマンドラインからProducerを起動可能です。

```bash
$ # JARファイルのビルド
$ mvn clean package -DskipTests

$ # JARファイルからProducerを起動
$ java -jar target/rest-producer-1.0-SNAPSHOT-jar-with-dependencies.jar
```


## イベントの送信方法

リクエストボディにユーザーID `userId`、コンテンツID `contentId`を含めて送信します。
採番されたオーダーID `orderId` がレスポンスボディとして返却されます。 

```bash
$ curl -X POST -H 'Content-Type: application/json' \
    -d '{"userId": "100", "contentId": "234"}' \
    http://localhost:7070/ticket
```
