package uk.me.eastmans.multi.em.kafka;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class DbChangeEventSerde implements
        Serializer<DbChangeEvent>, Deserializer<DbChangeEvent> {

    final private JsonDeserializer<DbChangeEvent> deserializer;
    final private JsonSerializer<DbChangeEvent> serializer;

    public DbChangeEventSerde() {
        deserializer = new JsonDeserializer<>(DbChangeEvent.class);
        serializer = new JsonSerializer<>();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    public byte[] serialize(String topic, DbChangeEvent data) {
        return serializer.serialize(topic, data);
    }

    public byte[] serialize(String topic, Headers headers, DbChangeEvent data) {
        return serializer.serialize(topic, headers, data);
    }

    public DbChangeEvent deserialize(String topic, byte[] data) {
        return deserializer.deserialize(topic, null, data);
    }

    public DbChangeEvent deserialize(String topic, Headers headers, byte[] data) {
        return deserializer.deserialize(topic, headers, data);
    }

    @Override
    public DbChangeEvent deserialize(String topic, Headers headers, ByteBuffer data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    public void close() {
    }
}