package com.netease.push.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProperties {

    private static Logger logger = LoggerFactory.getLogger(KafkaProperties.class);

    private String brokerAddress;
    private String group;
    private String topics;
    private Boolean autoCommit;
    private Integer maxPollInterval;
    private Integer maxPollCount;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getBrokerAddress() {
        return this.brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public void show() {
        logger.info("broker address : [{}], group : [{}], topic : [{}]",
                this.getBrokerAddress(), this.getGroup(), this.getTopics());
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Integer getMaxPollInterval() {
        return maxPollInterval;
    }

    public void setMaxPollInterval(Integer maxPollInterval) {
        this.maxPollInterval = maxPollInterval;
    }

    public Integer getMaxPollCount() {
        return maxPollCount;
    }

    public void setMaxPollCount(Integer maxPollCount) {
        this.maxPollCount = maxPollCount;
    }
}
