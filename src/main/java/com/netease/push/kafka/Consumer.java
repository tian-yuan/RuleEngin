package com.netease.push.kafka;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.netease.push.message.OnlineStatus;
import com.netease.push.message.deserializers.Deserializer;
import com.netease.push.message.deserializers.TokenDeserializer;
import com.netease.push.rule.DynamicRuleParser;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Consumer implements MessageListener<String, String>, ConfigChangeListener {

    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private DynamicRuleParser ruleParser;

    private Consumer(){
        Config config = ConfigService.getAppConfig();
        config.addChangeListener(this);
    }

    private Deserializer deserializer = new TokenDeserializer();
    private KafkaMessageListenerContainer<String, String> container;

    public void start(KafkaProperties properties) {
        Map<String, Object> map = new HashMap<>();
        map.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroup());
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBrokerAddress());
        map.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, properties.getAutoCommit());
        map.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, properties.getMaxPollInterval());
        map.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getMaxPollCount());
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(map);
        ContainerProperties containerProperties = new ContainerProperties(properties.getTopics().split(","));
        containerProperties.setMessageListener(this);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.start();
    }

    public void stop() {
        container.stop();
        container = null;
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        String message = data.value();
        if (message == null) {
            return;
        }
        logger.info("message = " + message);
        OnlineStatus onlineStatus = deserializer.deserialize(OnlineStatus.class, message);
        onlineStatus.setTopic(data.topic());
        ruleParser.evaluate(onlineStatus);
    }

    @Override
    public void onChange(ConfigChangeEvent configChangeEvent) {
        for (String key: configChangeEvent.changedKeys()) {
            ConfigChange change = configChangeEvent.getChange(key);
            if (key.equalsIgnoreCase("kafka")) {
                KafkaProperties kafkaProperties = JSON.parseObject(change.getNewValue(), KafkaProperties.class);
                if (kafkaProperties!=null) {
                    stop();
                    start(kafkaProperties);
                }
            }
        }
    }
}
