package com.netease.push.destination;

import com.alibaba.fastjson.JSON;
import com.netease.push.kafka.KafkaProperties;
import com.netease.push.kafka.Producer;
import com.netease.push.message.OnlineStatus;
import com.netease.push.rule.Action;
import com.netease.push.rule.Event;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
@Component
public class DestinationManager {
    private static Logger logger = LogManager.getLogger(DestinationManager.class);

    private static String TIPOC = "push.register.onlinestatus.newsclient";
    @Autowired
    private Producer producer;

    public void processMessage(Event event) {
        OnlineStatus onlineStatus = (OnlineStatus)event;
        if (onlineStatus != null) {
            Action action = onlineStatus.getAction();
            logger.info("action : " + JSON.toJSONString(action));
            String message = JSON.toJSONString(onlineStatus);
            logger.info("onlineStatus : " + message);
            producer.sendMessage(TIPOC, message);
        }
    }
}
