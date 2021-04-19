

package com.c123.producers;

        import java.util.Date;
        import java.util.Properties;

        import com.c123.model.Event;
        import org.apache.kafka.clients.producer.Producer;
        import org.apache.kafka.clients.producer.KafkaProducer;
        import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProducer222 {

    public static void main(String[] args) throws Exception {

        String topicName = "taltopic";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //"com.c123.serializers.TalSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        int i=0;
        try{
            while(true) {
                ProducerRecord<String, String> record1232 =
                        new ProducerRecord<>(topicName, Integer.toString(1), "TAL--" + i);
                Thread.sleep(1000);
                producer.send(record1232);
                i++;
            }
        }catch(Exception e){
            System.out.println("Error while sending: ");
            e.printStackTrace();
            producer.close();
        }
    }
}