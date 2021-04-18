
package com.c123.consumers;


import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import com.c123.model.Event;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.Document;

public class SimpleConsumer {
    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    // Creating a Mongo client
    static {
        mongo = new MongoClient("localhost", 27017);
        // Accessing the database
        database = mongo.getDatabase("eventDB");

        // Retrieving a collection
        collection = database.getCollection("events");
        System.out.println("Collection events selected successfully");
    }

    public static void main(String[] args) {
        try {
            int i = 0;
            KafkaConsumer<String, Event> consumer = createConsumer();
            consumer.subscribe(Arrays.asList("taltopic"));

            while (true) {
                try {

                    Duration duration = Duration.ofMillis(1_000);

                    ConsumerRecords<String, Event> records = consumer.poll(duration);
                    if (i >= 5) {
                        break;
                    } else if (records.isEmpty()) {
                        i++;
                    } else {
                        i = 0;
                        handleRecords(records); // Handle records
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }// ~while
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished successfully!");
    }



    public static void handleRecords(ConsumerRecords<String, Event> records) {
        Event e2;
        for (ConsumerRecord<String, Event> record : records) {
            System.out.printf("Offset = %d\n", record.offset());
            System.out.printf("Key    = %s\n", record.key());
            e2 = record.value();
            insert2DB(e2);
            System.out.printf("Value  = %s\n", e2.toString());
        }// ~for
    }

    public static void insert2DB(Event event){
        Document document = new Document("reporterId", event.getReporterId())
                .append("timestamp", event.getTimestamp())
                .append("metricId", event.getMetricId())
                .append("metricValue", event.getMetricValue())
                .append("message", event.getMessage());

        // Insert to collection
        collection.insertOne(document);
    }

    public static KafkaConsumer<String, Event> createConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "consumer-grp1");
        props.put("key.deserializer", StringDeserializer.class.getName());
        //props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", "com.c123.deserializers.TalDeserializer");
        props.put("auto.offset.reset", "earliest");

        return new KafkaConsumer<String, Event>(props);
    }
}