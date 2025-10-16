# multi
Tests multi maven modules

to build the entity-model just do 
~~~
mvn install
~~~

to build the vaadin gui do
~~~
cd vaadin-gui
mvn spring-boot:run
~~~

The entity manager packages are prefixed with uk.me.eastmans.multi.em
and the gui vaadin app is uk.me.eastmans.multi.gui.

## Kafka aspects
https://kafka.apache.org/quickstart

~~~
tar -xzf kafka_2.13-4.1.0.tgz
cd kafka_2.13-4.1.0

KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties

bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092

bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092
>This is my first event
>This is my second event

bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
This is my first event
This is my second event

rm -rf /tmp/kafka-logs /tmp/kraft-combined-logs
~~~
For the example kafka processor you will need to create the topic purchases using
~~~
bin/kafka-topics.sh --create --topic purchases --bootstrap-server localhost:9092
~~~
