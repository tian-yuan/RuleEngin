package com.netease.push.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hzliuzebo on 2018/10/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaPropertiesTest {
    @Test
    private void TestKafkaProperties() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setBrokerAddress("10.242.83.109:9092");
        kafkaProperties.setAutoCommit(true);
        kafkaProperties.setGroup("rule-engine");
        kafkaProperties.setMaxPollCount(1000);
        kafkaProperties.setMaxPollInterval(10);
        kafkaProperties.setTopics("newsOnlineStatus");
    }
}
