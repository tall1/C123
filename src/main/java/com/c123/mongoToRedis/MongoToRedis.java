package com.c123.mongoToRedis;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import redis.clients.jedis.Jedis;

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
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");

    // Creating a Mongo client
    static {
        mongo = new MongoClient("localhost", 27017);
        // Accessing the database
        database = mongo.getDatabase("eventDB");

        // Retrieving a collection
        collection = database.getCollection("events");

        // Jedis
        jedis = new Jedis();

        System.out.println("Collection events selected successfully");
    }

    public static void main(String[] args) throws InterruptedException, ParseException {
        MongoToRedis m = new MongoToRedis();
        String lastDateStr;
        if (jedis.get("last_date") == null) {
            LocalDate localDate = LocalDate.now().minusDays(10);
            last = java.util.Date.from(localDate.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
            lastDateStr = sdf.format(last);
            jedis.set("last_date", lastDateStr);
        }else{
            lastDateStr = jedis.get("last_date");
            last = sdf.parse(lastDateStr);
        }
        m.run();
    }

    @Override
    public void run() {
        String lastDateStr;
        Document doc;

        while (true) {

            FindIterable<Document> iterDoc =
                    collection.find(Filters.gt("timestamp", last))
                            .sort(new BasicDBObject("timestamp", 1));

            Iterator it = iterDoc.iterator();

            while (it.hasNext()) {
                doc = (Document) it.next();
                System.out.println(doc);
                Date date = (Date) doc.get("timestamp");
                int reporterId = (int) doc.get("reporterId");
                last = date;
                lastDateStr = sdf.format(last);
                jedis.set("last_date", lastDateStr);
            }
            int i =1;
            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
