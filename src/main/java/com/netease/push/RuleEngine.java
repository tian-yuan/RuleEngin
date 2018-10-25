package com.netease.push;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.netease.push.kafka.Consumer;
import com.netease.push.kafka.KafkaProperties;
import com.netease.push.kafka.Producer;
import com.netease.push.rule.Action;
import com.netease.push.rule.DynamicRuleParser;
import com.netease.push.rule.RuleMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class RuleEngine {
    public static void main( String[] args ) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RuleEngine.class, args);

        startDrool(ctx);
        startKafkaConsumer(ctx);
        startProducer(ctx);

        // start producer, JUST FOR TEST
//        Producer producer = new Producer();
//        KafkaProperties properties = new KafkaProperties();
//        properties.setBrokerAddress("192.168.153.130:9092");
//        producer.start(properties);
//        producer.sendMessage("onlinestatus", "deviceid\n1\n1223\nwww.netease.com");
    }

    private static void startProducer(ConfigurableApplicationContext ctx) {
        Producer producer = (Producer) ctx.getBean("producer");
        KafkaProperties properties = new KafkaProperties();
        properties.setBrokerAddress("192.168.153.130:9092");
        producer.start(properties);
    }

    // start kafka consumer
    private static void startKafkaConsumer(ConfigurableApplicationContext ctx) {
        Consumer consumer = (Consumer) ctx.getBean("consumer");
        Config config = ConfigService.getAppConfig();
        String kafkaConfig = config.getProperty("kafka", "");
        KafkaProperties properties;
        if (kafkaConfig.equalsIgnoreCase("")) {
            // default properties
            properties = new KafkaProperties();
//            properties.setBrokerAddress("192.168.153.130:9092");
            properties.setBrokerAddress("10.160.114.6:9092");
            properties.setAutoCommit(true);
            properties.setGroup("online-status-news");
            properties.setMaxPollCount(100);
            properties.setMaxPollInterval(2000);
            properties.setTopics("newsOnlineStatus");
        } else {
            properties = JSON.parseObject(kafkaConfig, KafkaProperties.class);
        }
        consumer.start(properties);
    }

    // start drool
    private static void startDrool(ConfigurableApplicationContext ctx) {
        DynamicRuleParser ruleParser = (DynamicRuleParser)ctx.getBean("dynamicRuleParser");
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setSql("select * from newsOnlineStatus");
        Action action = new Action();
        ruleMetadata.setAction(action);
        ruleParser.update(ruleMetadata);
    }
}
