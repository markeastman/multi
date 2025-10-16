package uk.me.eastmans.multi.em.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class PostKafkaEvent {
    private static final Logger logger = LoggerFactory.getLogger(PostKafkaEvent.class);
    private static final String TOPIC = "db-events";

    @Autowired
    private KafkaTemplate<String, DbChangeEvent> kafkaTemplate;

    public void postCreate(String entityName, Long... ids) {
        DbChangeEvent event = new DbChangeEvent(entityName, "c", ids);
        sendMessage(entityName, event);
    }

    public void postDelete(String entityName, Long... ids) {
        DbChangeEvent event = new DbChangeEvent(entityName, "d", ids);
        sendMessage(entityName, event);
    }

    public void postUpdate(String entityName, Long... ids) {
        DbChangeEvent event = new DbChangeEvent(entityName, "u", ids);
        sendMessage(entityName, event);
    }

    private void sendMessage(String key, DbChangeEvent value) {
        CompletableFuture<SendResult<String, DbChangeEvent>> future =
                kafkaTemplate.send(TOPIC, key, value);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", result.getRecordMetadata().topic(), key, value));
            } else {
                ex.printStackTrace(System.out);
            }
        });
    }
}