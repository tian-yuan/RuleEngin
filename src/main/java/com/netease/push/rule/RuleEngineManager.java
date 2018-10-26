package com.netease.push.rule;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.netease.push.kafka.Consumer;
import com.netease.push.kafka.KafkaProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hzliuzebo on 2018/10/26.
 */
public class RuleEngineManager implements ConfigChangeListener{
    private static Logger logger = LogManager.getLogger(RuleEngineManager.class);

    @Autowired
    private DynamicRuleParser dynamicRuleParser;

    @Autowired
    private Consumer consumer;

    private boolean isStarted = false;

    private static RuleEngineManager ruleEngineManager = new RuleEngineManager();

    public static RuleEngineManager getInstance() {
        return ruleEngineManager;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start() {
        updateRule();
        startKafkaConsumer();
    }

    public void stop() {
        stopKafkaConsumer();
    }

    private void updateRule() {
        Config config = ConfigService.getAppConfig();
        String ruleConfig = config.getProperty("rule", "");
        RuleMetadata ruleMetadata = JSON.parseObject(ruleConfig, RuleMetadata.class);
        if (ruleMetadata == null) {
            logger.error("get rule config error, config : " + ruleConfig);
            return;
        }
        dynamicRuleParser.update(ruleMetadata);
    }

    private void startKafkaConsumer() {
        Config config = ConfigService.getAppConfig();
        String kafkaConfig = config.getProperty("kafka", "");
        KafkaProperties properties;
        if (kafkaConfig.equalsIgnoreCase("")) {
            // default properties
            properties = new KafkaProperties();
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

    private void stopKafkaConsumer() {
        consumer.stop();
    }

    @Override
    public void onChange(ConfigChangeEvent configChangeEvent) {
        for (String key : configChangeEvent.changedKeys()) {
            ConfigChange change = configChangeEvent.getChange(key);
            logger.info("Change - key: " + change.getPropertyName() + " , oldValue: " +
                    change.getOldValue() + " , newValue: " +
                    change.getNewValue() + " , changeType: " + change.getChangeType());
            if (key.equals("kafka")) {
                RuleMetadata ruleMetadata = JSON.parseObject(change.getNewValue(), RuleMetadata.class);
                if (ruleMetadata != null) {
                    updateRule();
                }
            }
        }
    }
}
