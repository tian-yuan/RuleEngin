package com.netease.push.rule;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


/**
 * Created by hzliuzebo on 2018/10/18.
 */
public class ActionTest {
    private static Logger logger = LogManager.getLogger(ActionTest.class);
    @Test
    public void shouldTestSuccess() {
        Action action = new Action();
//        Kafka kafka = new Kafka();
//        kafka.setBrokers("localhost:9092");
//        kafka.setTopic("test");
//        action.setKafka(kafka);
        logger.info("action json string : " + JSON.toJSONString(action));
        logger.info("action : " + action.toString());
    }
}
