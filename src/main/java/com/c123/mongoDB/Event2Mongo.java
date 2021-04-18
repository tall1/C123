package com.c123.mongoDB;

import com.c123.model.Event;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

public class Event2Mongo {
    public static void main(String[] args) {
        Event e = new Event(1, new Date(), 1, 1 + 100, "Event" + 1);

        Document document = new Document("reporterId", e.getReporterId())
                .append("timestamp", e.getTimestamp())
                .append("metricId", e.getMetricId())
                .append("metricValue", e.getMetricValue())
                .append("message", e.getMessage());


        // Creating a Mongo client
        MongoClient mongo = new MongoClient("localhost", 27017);

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        // Insert to collection
        collection.insertOne(document);
    }
}
