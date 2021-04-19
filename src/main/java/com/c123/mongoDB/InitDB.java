package com.c123.mongoDB;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class InitDB {
    public static void main(String[] args) {
        // Creating a Mongo client
        MongoClient mongo = new MongoClient("localhost", 27017);
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        //Creating a collection
        database.createCollection("sampleCollection");
        System.out.println("Collection created successfully");

        /*// Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");*/

        System.out.println("Collection sampleCollection selected successfully");
    }
}
