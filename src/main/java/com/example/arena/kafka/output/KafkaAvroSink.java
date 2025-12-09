package com.example.arena.kafka.output;

import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelinePayload;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class KafkaAvroSink implements OutputSink<String> {

    private static final Logger log = LoggerFactory.getLogger(KafkaAvroSink.class);

    private final KafkaProducer<String, GenericRecord> producer;
    private final String topic;
    private final Schema schema;

    public KafkaAvroSink(String topic,
                         Properties baseProps,
                         Schema schema) {

        this.topic = topic;
        this.schema = schema;

        Properties props = new Properties();
        props.putAll(baseProps);

        props.putIfAbsent("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", KafkaAvroSerializer.class.getName());

        this.producer = new KafkaProducer<>(props);
        log.info("KafkaAvroSink created for topic='{}'", topic);
    }

    @Override
    public void write(PipelinePayload<String> payload) throws Exception {
        GenericRecord record = new GenericData.Record(schema);
        record.put("customerId", payload.id());
        record.put("name", payload.data()); // simple example: put raw data into name
        record.put("tier", "STANDARD");

        ProducerRecord<String, GenericRecord> kafkaRecord =
                new ProducerRecord<>(topic, payload.id(), record);

        producer.send(kafkaRecord); // async for throughput
    }

    public void close() {
        producer.close();
    }

    public static Schema loadSchemaFromResource(String resourcePath) throws Exception {
        try (InputStream in = KafkaAvroSink.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Schema resource not found: " + resourcePath);
            }
            return new Schema.Parser().parse(in);
        }
    }
}
