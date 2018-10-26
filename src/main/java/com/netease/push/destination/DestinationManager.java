package com.netease.push.destination;

import com.alibaba.fastjson.JSON;
import com.netease.push.kafka.KafkaProperties;
import com.netease.push.kafka.Producer;
import com.netease.push.message.OnlineStatus;
import com.netease.push.rule.Action;
import com.netease.push.rule.Event;
import com.netease.push.rule.Kafka;
import com.netease.push.rule.Tsdb;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
@Component
public class DestinationManager {
    private static Logger logger = LogManager.getLogger(DestinationManager.class);

    private Map<String, Producer> kafkaProducerMap = new HashMap<>();

    public void processMessage(Event event) {
        OnlineStatus onlineStatus = (OnlineStatus)event;
        if (onlineStatus != null) {
            Action action = onlineStatus.getAction();
            logger.info("action : " + JSON.toJSONString(action));
            Kafka kafka = action.getKafka();
            if (kafka != null) {
                // send message to kafka
                String key = kafka.getBrokers();
                if (kafkaProducerMap.get(key) == null) {
                    Producer producer = new Producer();
                    KafkaProperties kafkaProperties = new KafkaProperties();
                    kafkaProperties.setBrokerAddress(kafka.getBrokers());
                    producer.start(kafkaProperties);
                    kafkaProducerMap.put(key, producer);
                }
                kafkaProducerMap.get(key).sendMessage(kafka.getTopic(), JSON.toJSONString(onlineStatus));
            }

            Tsdb tsdb = action.getTsdb();
            if (tsdb != null) {
                // send message to tsdb
                logger.info("tsdb is not supported now.");
            }
        }
    }
}
