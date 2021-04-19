package com.c123.mongoToRedis;

import com.c123.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;

public class MongoToRedis extends Thread {

    private static MongoClient mongo;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static Jedis jedis;
    private static Date last;
    private static SimpleDateFormat sdf;

    // Creating a Mongo client
    static {
        mongo = new MongoClient("localhost", 27017);
        // Accessing the database
        database = mongo.getDatabase("eventDB");
        // Retrieving a collection
        collection = database.getCollection("events");
        // Jedis
        jedis = new Jedis();
        // SDF
        sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");

    }

    public static void main(String[] args) throws InterruptedException, ParseException {
        MongoToRedis m = new MongoToRedis();
        setJedis();
        m.start();
    }

    @Override
    public void run() {
        String lastDateStr, key, value;
        Document doc;
        Event curEvent;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(sdf);

        while (true) {
            // Retrieve Documents from mongo with filter, sorted
            FindIterable<Document> iterDoc =
                    collection.find(Filters.gt("timestamp", last))
                              .sort(new BasicDBObject("timestamp", 1));

            Iterator it = iterDoc.iterator();

            while (it.hasNext()) {
                doc = (Document) it.next();
                System.out.println(doc);

                Date date = (Date) doc.get("timestamp");
                int reporterId = (int) doc.get("reporterId");
                last = date; // keep the last date
                curEvent = doc2Event(doc);

                lastDateStr = sdf.format(last); // turn to format
                key = reporterId + ":" + lastDateStr;
                try {
                    value = objectMapper.writeValueAsString(curEvent);
                } catch (JsonProcessingException e) {
                    System.out.println("Error converting Event to String:");
                    e.printStackTrace();
                    break;
                }

                jedis.set(key, value);
                jedis.set("last_date", lastDateStr);
            }// ~while it.hasnext()

            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }// ~while
    }

    public static Event doc2Event(Document doc) {
        int repId = (int) doc.get("reporterId");
        Date timestamp = (Date) doc.get("timestamp");
        int metricId = (int) doc.get("metricId");
        int metricValue = (int) doc.get("metricValue");
        String message = (String) doc.get("message");
        return new Event(repId, timestamp, metricId, metricValue, message);
    }

    public static void setJedis() throws ParseException {
        String lastDateStr;
        if (jedis.get("last_date") == null) {
            LocalDate localDate = LocalDate.now().minusDays(10);
            last = java.util.Date.from(localDate.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
            lastDateStr = sdf.format(last);
            jedis.set("last_date", lastDateStr);
        } else {
            lastDateStr = jedis.get("last_date");
            last = sdf.parse(lastDateStr);
        }
    }
}
