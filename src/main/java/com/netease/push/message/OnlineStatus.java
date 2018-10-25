package com.netease.push.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.netease.push.message.deserializers.Key;
import com.netease.push.rule.Action;
import com.netease.push.rule.Event;
import lombok.Data;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
@Data
public class OnlineStatus extends Event {

    @JSONField(serialize=false)
    public String topic;
    @Key(0)
    public String deviceId;
    @Key(1)
    public String onlineStatus;
    @Key(2)
    public String eventTime;
    @Key(3)
    public String domain;
}
