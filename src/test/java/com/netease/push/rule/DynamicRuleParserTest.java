package com.netease.push.rule;

import com.netease.push.message.OnlineStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicRuleParserTest {
    @Autowired
    private DynamicRuleParser dynamicRuleParser;

    @Test
    public void shoudlTestDynamicRuleParserSuccess() {
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setSql("select * from test");
        ruleMetadata.setRuleDisabled(true);
        ruleMetadata.setNpnsIotSqlVersion("2018-10-22");
        Action action = new Action();
        Kafka kafka = new Kafka();
        kafka.setBrokers("localhost:9092");
        kafka.setTopic("test");
        action.setKafka(kafka);
        ruleMetadata.setAction(action);
        dynamicRuleParser.update(ruleMetadata);
        OnlineStatus onlineStatus = new OnlineStatus();
        onlineStatus.setTopic("test");
        onlineStatus.setDeviceId("deviceId");
        onlineStatus.setOnlineStatus(1);
        onlineStatus.setEventTime(new Date().getTime());
        onlineStatus.setDomain("news.163.com");
        dynamicRuleParser.evaluate(onlineStatus);
        onlineStatus.setTopic("two");
        dynamicRuleParser.evaluate(onlineStatus);
    }
}
