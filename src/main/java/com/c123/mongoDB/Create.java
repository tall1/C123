package com.c123.mongoDB;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Create {
    public static void main(String[] args) {
// Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");
        Document document1 = new Document("title", "MongoDB")
                .append("description", "database")
                .append("likes", 100)
                .append("url", "http://www.tutorialspoint.com/mongodb/")
                .append("by", "tutorials point");

        Document document2 = new Document("title", "Tal's DB")
                .append("description", "database")
                .append("likes", 300)
                .append("url", "https://www.github.com/tall1/")
                .append("by", "Tal's github");
        //It is possible to insert one document using MongoCollection<T>.inserOne()
        //Inserting document into the collection
        //collection.insertOne(document);

        List<Document> list = new ArrayList<Document>();
        list.add(document1);
        list.add(document2);
        collection.insertMany(list);
        System.out.println("Documents inserted successfully");
    }
}
