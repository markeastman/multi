package uk.me.eastmans.multi.dbevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import uk.me.eastmans.multi.em.kafka.DbChangeEvent;

@Service
public class DbEventConsumer {
    private final Logger logger = LoggerFactory.getLogger(DbEventConsumer.class);

    @KafkaListener(id = "dbEventConsumer", topics = "db-events", groupId = "spring-boot", autoStartup = "false")
    public void listen(DbChangeEvent value,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info(String.format(
                "Consumed event from topic %s: key = %-10s value = %s",
                topic, key, value));
    }
}