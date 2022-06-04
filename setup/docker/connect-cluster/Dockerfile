FROM confluentinc/cp-kafka-connect-base:7.1.1

ENV CONNECT_PLUGIN_PATH="/usr/share/java,/usr/share/confluent-hub-components"

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.5.0 \
      && confluent-hub install --no-prompt confluentinc/kafka-connect-s3:10.0.8 \
      && confluent-hub install --no-prompt jcustenborder/kafka-connect-redis:0.0.2.17

RUN mkdir /usr/share/java/kafka-connect-jdbc \
      && wget -P /usr/share/java/kafka-connect-jdbc https://jdbc.postgresql.org/download/postgresql-42.3.6.jar

