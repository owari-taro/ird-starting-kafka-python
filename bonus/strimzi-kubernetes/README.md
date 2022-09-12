# Kafka on Kubernetes

KubernetesにKafkaクラスタを構築します。

## 事前準備

KubernetesにKafkaクラスタおよび関連リソースを立ち上げるためにStrimziをインストールする。
StrimziはKubernetes Operatorである。
カスタムリソースを作成することで、Operatorは必要なコンポーネントを作成します。

### Operator Lifecycle Managerのインストール

Operatorを管理するOLMをインストールします。

```shell
$ curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/v0.22.0/install.sh | bash -s v0.22.0
customresourcedefinition.apiextensions.k8s.io/... created
...
namespace/olm created
namespace/operators created
...
Waiting for deployment "olm-operator" rollout to finish: 0 of 1 updated replicas are available...
deployment "olm-operator" successfully rolled out
Waiting for deployment "catalog-operator" rollout to finish: 0 of 1 updated replicas are available...
deployment "catalog-operator" successfully rolled out
Package server phase: Installing
Package server phase: Succeeded
deployment "packageserver" successfully rolled out
```

スクリプト実行後にOLMのPodが起動していることを確認します。

```shell
$ kubectl get po -n olm -w
NAME                                READY   STATUS         RESTARTS   AGE
catalog-operator-865494bfbf-8tz6k   1/1     Running        0          62s
olm-operator-f7746965-b52xp         1/1     Running        0          62s
operatorhubio-catalog-bzxrc         1/1     Running        0          29s
packageserver-6d6f865645-vhcgh      1/1     Running        0          31s
packageserver-6d6f865645-wwm46      1/1     Running        0          31s
```

### Strimziのインストール

OLMのSubscriptionリソースを作成してStrimziをインストールします。

```shell
$ cat strimzi-kafka-operator.yaml
apiVersion: operators.coreos.com/v1alpha1
kind: Subscription
metadata:
  name: ird-strimzi-kafka-operator
  namespace: operators
spec:
  channel: stable
  name: strimzi-kafka-operator
  source: operatorhubio-catalog
  sourceNamespace: olm

$ kubectl apply -f strimzi-kafka-operator.yaml
subscription.operators.coreos.com/ird-strimzi-kafka-operator created
```

ClusterServiceVersionリソースを確認してStrimziのステータスが `Succeeded` になるまで待機します。

```shell
$ kubectl get csv -n operators -w
NAME                               DISPLAY   VERSION   REPLACES                           PHASE
strimzi-cluster-operator.v0.31.0   Strimzi   0.31.0    strimzi-cluster-operator.v0.30.0   Installing
strimzi-cluster-operator.v0.31.0   Strimzi   0.31.0    strimzi-cluster-operator.v0.30.0   Succeeded
```

## Kafkaクラスタの作成

Namespaceを作成する。

```shell
$ kubectl apply -f namespace-ird-kafka.yaml        
namespace/ird-kafka created
```

Kafkaリソースを適用してKafkaクラスタを作成する。

```shell
$ kubectl apply -f kafka-ird-cluster.yaml
kafka.kafka.strimzi.io/ird-cluster created
```

KafkaクラスタがReadyであることを確認する。

```shell
$ kubectl get kafka -n ird-kafka
NAME          DESIRED KAFKA REPLICAS   DESIRED ZK REPLICAS   READY   WARNINGS
ird-cluster   3                        3                     True

$ kubectl get pod -n ird-kafka
get po -n ird-kafka   
NAME                                           READY   STATUS    RESTARTS   AGE
ird-cluster-entity-operator-65d756ccd8-kfp9s   3/3     Running   0          28s
ird-cluster-kafka-0                            1/1     Running   0          51s
ird-cluster-kafka-1                            1/1     Running   0          51s
ird-cluster-kafka-2                            1/1     Running   0          51s
ird-cluster-zookeeper-0                        1/1     Running   0          2m53s
ird-cluster-zookeeper-1                        1/1     Running   0          2m53s
ird-cluster-zookeeper-2                        1/1     Running   0          2m53s
```

## Topicの作成

Topicリソースを適用して、Kafka Topicを作成します。

```shell
$ cat topic-ticket-order.yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaTopic
metadata:
  name: ticket-order
  namespace: ird-kafka
  labels:
    strimzi.io/cluster: ird-cluster
spec:
  partitions: 6
  replicas: 3

$ kubectl apply -f topic-ticket-order.yaml
kafkatopic.kafka.strimzi.io/ticket-order created

$ kubectl get kafkatopic ticket-order -n ird-kafka
NAME           CLUSTER       PARTITIONS   REPLICATION FACTOR   READY
ticket-order   ird-cluster   6            3                    True
```
