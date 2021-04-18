package com.c123.deserializers;

import com.c123.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


public class TalDeserializer implements Deserializer {
    @Override
    public Event deserialize(String topic, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        Event event = null;
        try {
            event = mapper.readValue(bytes, Event.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public Event deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public void close() {

    }
}
