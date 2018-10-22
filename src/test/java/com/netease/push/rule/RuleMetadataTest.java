package com.netease.push.rule;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzliuzebo on 2018/10/18.
 */
public class RuleMetadataTest {
    private static Logger logger = LogManager.getLogger(RuleMetadataTest.class);

    @Test
    public void shouldTestSuccess() {
        RuleMetadata ruleMetadata = new RuleMetadata();
        logger.info("test set sql.");
        ruleMetadata.setSql("select * from device.info");
        ruleMetadata.setRuleDisabled(true);
        ruleMetadata.setNpnsIotSqlVersion("2018-10-18");

        Action action = new Action();
        Kafka kafka = new Kafka();
        kafka.setBrokers("localhost:9092");
        kafka.setTopic("test");
        action.setKafka(kafka);
        List<Action> actionList = new ArrayList<>();
        actionList.add(action);
        ruleMetadata.setActions(actionList);
        logger.info("rule metadata : " + JSON.toJSONString(ruleMetadata));
    }
}
