package com.netease.push.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Producer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public void start(KafkaProperties properties) {
        Map<String, Object> map = new HashMap<>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBrokerAddress());
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory producerFactory = new DefaultKafkaProducerFactory(map);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    public void sendMessage(String topic, String message){
        kafkaTemplate.send(topic, message);
    }

}
