package com.c123.serializers;

import com.c123.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class TalSerializer implements Serializer<Event> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Event event) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(event).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Event event) {
        byte[] retVal = null;
        String str;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            str = objectMapper.writeValueAsString(event);
            retVal = str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
