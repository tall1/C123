package com.c123.producers;

import com.c123.model.Event;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class KafkaProducer {
    public static void main(String[] args) throws Exception {
        // Hi!
        String topicName = "taltopic";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", //"org.apache.kafka.common.serialization.StringSerializer");
                "com.c123.serializers.TalSerializer");

        Producer<String, Event> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);

        try {
            String LOCK_FL = "/home/tal/IdeaProjects/C123/src/main/java/com/c123/producers/producerRunCondition.txt";
            //String LOCK_FL = "C:\\Users\\talev\\IdeaProjects\\ApacheKafka\\src\\main\\java\\com\\c123\\producers\\producerRunCondition.txt";
            File myFile = new File(LOCK_FL);
            Scanner myReader = new Scanner(myFile);
            for (int i = 0; myReader.nextInt() != 0; i++) {

                Event e1 = new Event(i, new Date(), i, i + 100, "Event" + (i + 1));
                ProducerRecord<String, Event> record =
                        new ProducerRecord<>(topicName, Integer.toString(1), e1);
                producer.send(record);

                myReader.close();
                myReader = new Scanner(myFile);

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Error writing message:");
            e.printStackTrace();
        } finally {
            System.out.println("Finished");
            producer.close();
        }

    }
}
